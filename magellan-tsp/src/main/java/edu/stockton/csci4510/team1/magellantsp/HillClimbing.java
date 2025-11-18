package edu.stockton.csci4510.simulatedAnnealing;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.Configurator;
import org.cicirello.search.SolutionCostPair;
import org.cicirello.search.hc.FirstDescentHillClimber;
import org.cicirello.search.hc.SteepestDescentHillClimber;
import org.cicirello.search.operators.permutations.InsertionMutation;
import org.cicirello.search.operators.permutations.PermutationInitializer;
import org.cicirello.search.operators.permutations.ReversalMutation;
import org.cicirello.search.operators.permutations.SwapMutation;
import org.cicirello.search.problems.tsp.TSP;


public class HillClimbing {

    static class City {
        final String name;
        final double lat, lon;
        City(String name, double lat, double lon) {
            this.name = name; this.lat = lat; this.lon = lon;
        }
    }

    static List<City> loadAirports(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv);
        if (lines.isEmpty()) throw new IllegalArgumentException("empty csv");

        String[] headers = lines.get(0).split(",");
        Map<String,Integer> idx = new HashMap<>();
        for (int i = 0; i < headers.length; i++)
            idx.put(headers[i].trim().toLowerCase(), i);

        Integer iName = idx.getOrDefault("name", idx.getOrDefault("airport_name", 0));
        Integer iLat  = idx.getOrDefault("latitude_deg", idx.getOrDefault("lat", 1));
        Integer iLon  = idx.getOrDefault("longitude_deg", idx.getOrDefault("lon", 2));

        List<City> list = new ArrayList<>();
        for (int r = 1; r < lines.size(); r++) {
            String line = lines.get(r).trim();
            if (line.isEmpty()) continue;
            String[] t = line.split(",");
            try {
                String name = t[iName].trim();
                double lat = Double.parseDouble(t[iLat].trim());
                double lon = Double.parseDouble(t[iLon].trim());
                list.add(new City(name, lat, lon));
            } catch (Exception ignore) {
                // skip malformed lines
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        // Default CSV path (same default as your example). Override by passing path as first arg.
        String fallback = System.getProperty("user.home") +
        "/IdeaProjects/ProjectTwo/magellan-tsp/magellan-tsp/src/main/resources/international_airports.csv";

        Path csv = Paths.get(args.length > 0 ? args[0] : fallback);

        if (!Files.exists(csv)) {
            throw new FileNotFoundException("CSV file not found: " + csv);
        }


        if (!Files.exists(csv)) {
            System.err.println("Airport CSV not found at: " + csv.toString());
            System.err.println("Pass path as first argument or place CSV at the default location.");
            System.exit(2);
        }

        List<City> cities = loadAirports(csv);
        int n = cities.size();
        System.out.println("Loaded " + n + " airports.");

        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < n; i++) {
            xs[i] = cities.get(i).lon; // lon -> x
            ys[i] = cities.get(i).lat; // lat -> y
        }

        // Build TSP instance from coordinates
        TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(xs, ys);

        // Configure RNG used by chips-n-salsa (optional, mirrors your other snippet)
        Configurator.configureRandomGenerator(213);

        // initializer
        PermutationInitializer init = new PermutationInitializer(n);

        final int restarts = 10; // number of random restarts per search (same idea as your SA optimize iterations)
        // FIRST-DESCENT (first improvement) hill climbers
        FirstDescentHillClimber<Permutation> hcFirstReversal =
            new FirstDescentHillClimber<>(tsp, new ReversalMutation(), init);
        SolutionCostPair<Permutation> solutionHCFirstReversal = hcFirstReversal.optimize(restarts);

        FirstDescentHillClimber<Permutation> hcFirstInsertion =
            new FirstDescentHillClimber<>(tsp, new InsertionMutation(), init);
        SolutionCostPair<Permutation> solutionHCFirstInsertion = hcFirstInsertion.optimize(restarts);

        FirstDescentHillClimber<Permutation> hcFirstSwap =
            new FirstDescentHillClimber<>(tsp, new SwapMutation(), init);
        SolutionCostPair<Permutation> solutionHCFirstSwap = hcFirstSwap.optimize(restarts);

        // STEEPEST-DESCENT hill climbers
        SteepestDescentHillClimber<Permutation> hcSteepReversal =
            new SteepestDescentHillClimber<>(tsp, new ReversalMutation(), init);
        SolutionCostPair<Permutation> solutionHCSteepReversal = hcSteepReversal.optimize(restarts);

        SteepestDescentHillClimber<Permutation> hcSteepInsertion =
            new SteepestDescentHillClimber<>(tsp, new InsertionMutation(), init);
        SolutionCostPair<Permutation> solutionHCSteepInsertion = hcSteepInsertion.optimize(restarts);

        SteepestDescentHillClimber<Permutation> hcSteepSwap =
            new SteepestDescentHillClimber<>(tsp, new SwapMutation(), init);
        SolutionCostPair<Permutation> solutionHCSteepSwap = hcSteepSwap.optimize(restarts);

        // Print results using same style as your SA example
        System.out.println("------------------------------");
        System.out.println("HILL CLIMBERS");
        System.out.println("------------------------------");
        System.out.printf("%-20s%15s%n", "search_operator", "distance");
        System.out.println("------------------------------");
        System.out.printf("%-20s%15.3f%n", "first_reversal", solutionHCFirstReversal.getCost());
        System.out.printf("%-20s%15.3f%n", "first_insertion", solutionHCFirstInsertion.getCost());
        System.out.printf("%-20s%15.3f%n", "first_swap", solutionHCFirstSwap.getCost());
        System.out.printf("%-20s%15.3f%n", "steepest_reversal", solutionHCSteepReversal.getCost());
        System.out.printf("%-20s%15.3f%n", "steepest_insertion", solutionHCSteepInsertion.getCost());
        System.out.printf("%-20s%15.3f%n", "steepest_swap", solutionHCSteepSwap.getCost());
        System.out.println("------------------------------");

        // choose the best of all runs
        List<SolutionCostPair<Permutation>> all = Arrays.asList(
            solutionHCFirstReversal, solutionHCFirstInsertion, solutionHCFirstSwap,
            solutionHCSteepReversal, solutionHCSteepInsertion, solutionHCSteepSwap);

        SolutionCostPair<Permutation> best = all.get(0);
        for (SolutionCostPair<Permutation> s : all) {
            if (s.getCost() < best.getCost()) best = s;
        }

        System.out.printf("%nBest tour cost: %.3f%n", best.getCost());
        System.out.println("Tour order (indices in CSV): " + Arrays.toString(best.getSolution().toArray()));

        System.out.println("\nTour names:");
        for (int i = 0; i < n; i++) {
            int idx = best.getSolution().get(i);
            System.out.println((i + 1) + ". " + cities.get(idx).name);
        }
    }
}
