package edu.stockton.csci4510.simulatedAnnealing;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.cicirello.permutations.Permutation;
import org.cicirello.search.ProgressTracker;
import org.cicirello.search.SolutionCostPair;
import org.cicirello.search.operators.Initializer;
import org.cicirello.search.operators.UndoableMutationOperator;
import org.cicirello.search.operators.permutations.PermutationInitializer;
import org.cicirello.search.operators.permutations.TwoChangeMutation;
import org.cicirello.search.problems.OptimizationProblem;
import org.cicirello.search.sa.SimulatedAnnealing;

import edu.stockton.csci4510.team1.magellantsp.HaversineDistance;

public class SimulatedAnnealingTSP {

    private static final int ITERATIONS = 200_000;
    private static final int RUNS = 5;

    // container for airport data
    static class City {
        final String name;
        final double latDeg;
        final double lonDeg;

        City(String name, double latDeg, double lonDeg) {
            this.name = name;
            this.latDeg = latDeg;
            this.lonDeg = lonDeg;
        }
    }

    // Load airport data
    static List<City> loadAirports(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("CSV is empty: " + csv);
        }

        String[] headers = lines.get(0).split(",");
        Map<String, Integer> idx = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            idx.put(headers[i].trim().toLowerCase(), i);
        }

        // airport name, latitude, and longitude
        int iName = idx.getOrDefault("name",
                idx.getOrDefault("airport_name",
                        idx.getOrDefault("airport", -1)));

        int iLat = idx.getOrDefault("latitude_deg",
                idx.getOrDefault("lat_deg",
                        idx.getOrDefault("latitude",
                                idx.getOrDefault("lat", -1))));

        int iLon = idx.getOrDefault("longitude_deg",
                idx.getOrDefault("lon_deg",
                        idx.getOrDefault("longitude",
                                idx.getOrDefault("lon", -1))));

        if (iName < 0 || iLat < 0 || iLon < 0) {
            throw new IllegalArgumentException(
                    "CSV formatting off.");
        }

        List<City> list = new ArrayList<>();

        // Parse each row into a City if possible
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] t = line.split(",");
            if (t.length <= Math.max(iName, Math.max(iLat, iLon))) {
                continue;
            }

            try {
                String name = t[iName].trim();
                double lat = Double.parseDouble(t[iLat].trim());
                double lon = Double.parseDouble(t[iLon].trim());
                list.add(new City(name, lat, lon));
            } catch (Exception ignore) {
                // Skip malformed rows
            }
        }

        if (list.size() < 3) {
            throw new IllegalArgumentException("Need at least 3 valid rows; found " + list.size());
        }

        return list;
    }

    // Distance matrix builder using group's Haversine
    static double[][] buildDistanceMatrix(List<City> cities) {
        int n = cities.size();
        double[][] d = new double[n][n];

        for (int i = 0; i < n; i++) {
            d[i][i] = 0.0;

            City a = cities.get(i);
            for (int j = i + 1; j < n; j++) {
                City b = cities.get(j);

                // Use group's Haversine for distance in km
                double dist = HaversineDistance.distanceKm(
                        a.latDeg, a.lonDeg,
                        b.latDeg, b.lonDeg
                );

                d[i][j] = dist;
                d[j][i] = dist;
            }
        }
        return d;
    }

    // Calculate the total cost of a given tour
    static double tourCost(Permutation tour, double[][] d) {
        double cost = 0.0;
        int n = tour.length();

        for (int i = 0; i < n; i++) {
            int u = tour.get(i);
            int v = tour.get((i + 1) % n); // wrap back to start to close tour
            cost += d[u][v];
        }
        return cost;
    }

    // Wraps TSP as an OptimizationProblem for Chips-n-Salsa
    static class TSPProblem implements OptimizationProblem<Permutation> {
        private final double[][] d;

        TSPProblem(double[][] d) {
            this.d = d;
        }

        @Override
        public double cost(Permutation tour) {
            return tourCost(tour, d);
        }

        @Override
        public double value(Permutation tour) {
            return tourCost(tour, d);
        }

        @Override
        public double minCost() {
            return 0.0;
        }
    }

    // Store summary info for each SA run
    static class RunSummary {
        final int runNumber;
        final double costKm;
        final double seconds;
        final int firstIdx;
        final int lastIdx;

        RunSummary(int runNumber, double costKm, double seconds, int firstIdx, int lastIdx) {
            this.runNumber = runNumber;
            this.costKm = costKm;
            this.seconds = seconds;
            this.firstIdx = firstIdx;
            this.lastIdx = lastIdx;
        }
    }

    public static void main(String[] args) throws Exception {
        // airport csv pat
        Path csv = Paths.get("magellan-tsp/src/main/resources/international_airports.csv");

        // Load airport data
        List<City> cities = loadAirports(csv);
        int n = cities.size();
        System.out.println("Loaded " + n + " airports.");
        System.out.println("Iterations per run: " + String.format("%,d", ITERATIONS));
        System.out.println("Total runs: " + RUNS);
        System.out.println();

        // Build distance matrix from latitude/longitude
        double[][] d = buildDistanceMatrix(cities);

        // Create TSP optimization problem for Simulated Annealing
        OptimizationProblem<Permutation> problem = new TSPProblem(d);

        // List to hold summaries of each run
        List<RunSummary> summaries = new ArrayList<>();

        // Perform multiple independent SA runs
        for (int run = 1; run <= RUNS; run++) {
            Initializer<Permutation> init = new PermutationInitializer(n);

            // Two-change mutation (swap two positions in the permutation)
            UndoableMutationOperator<Permutation> move = new TwoChangeMutation();

            // Track progress and best-so-far solution
            ProgressTracker<Permutation> tracker = new ProgressTracker<>();

            // Simulated Annealing instance - Chips-n-Salsa
            SimulatedAnnealing<Permutation> sa = new SimulatedAnnealing<>(problem, move, init, tracker);

            // Measure runtime of current run
            long startNano = System.nanoTime();
            SolutionCostPair<Permutation> best = sa.optimize(ITERATIONS);
            long endNano = System.nanoTime();
            double elapsedSeconds = (endNano - startNano) / 1_000_000_000.0;

            double bestCost = best.getCostDouble();
            Permutation bestTour = best.getSolution();
            int firstIdx = bestTour.get(0);
            int lastIdx = bestTour.get(n - 1);

            // Save summary
            summaries.add(new RunSummary(run, bestCost, elapsedSeconds, firstIdx, lastIdx));

            System.out.printf(
                    "Run %d finished: cost = %.3f km, time = %.3f s%n",
                    run, bestCost, elapsedSeconds);
        }

        // Sort runs by cost so we can see the best ones first
        summaries.sort(Comparator.comparingDouble(r -> r.costKm));

        System.out.println();
        System.out.println("===== Top " + RUNS + " runs by tour cost =====");

        // Print readable summary for each run
        for (int rank = 0; rank < summaries.size(); rank++) {
            RunSummary r = summaries.get(rank);
            City startCity = cities.get(r.firstIdx);
            City endCity = cities.get(r.lastIdx);

            System.out.printf(
                    "#%d  (Run %d)%n" +
                            "  Cost: %.3f km%n" +
                            "  Time: %.3f s%n" +
                            "  Start: %s (index %d)%n" +
                            "  End:   %s (index %d)%n%n",
                    rank + 1,
                    r.runNumber,
                    r.costKm,
                    r.seconds,
                    startCity.name,
                    r.firstIdx,
                    endCity.name,
                    r.lastIdx);
        }
    }
}
