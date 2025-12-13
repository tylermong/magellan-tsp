package edu.stockton.csci4510.simulatedAnnealing;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import edu.stockton.csci4510.team1.magellantsp.HaversineDistance;

public class SimulatedAnnealingTSP {

    private static final int ITERATIONS = 200_000;
    private static final int RUNS = 5;

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

    static List<City> loadAirports(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv);
        if (lines.isEmpty()) throw new IllegalArgumentException("CSV is empty: " + csv);

        String[] headers = lines.get(0).split(",");
        Map<String, Integer> idx = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            idx.put(headers[i].trim().toLowerCase(), i);
        }

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
            throw new IllegalArgumentException("Missing required columns in CSV.");
        }

        List<City> list = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty())
                continue;

            String[] t = line.split(",");
            if (t.length <= Math.max(iName, Math.max(iLat, iLon)))
                continue;

            try {
                String name = t[iName].trim();
                double lat = Double.parseDouble(t[iLat].trim());
                double lon = Double.parseDouble(t[iLon].trim());
                list.add(new City(name, lat, lon));
            } catch (Exception ignore) {}
        }

        return list;
    }

    // Distance matrix builder using group's Haversine implementation
    static double[][] buildDistanceMatrix(List<City> cities) {
        int n = cities.size();
        double[][] d = new double[n][n];

        for (int i = 0; i < n; i++) {
            d[i][i] = 0.0;

            City a = cities.get(i);
            for (int j = i + 1; j < n; j++) {
                City b = cities.get(j);

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
    static double tourCost(int[] tour, double[][] d) {
        double cost = 0.0;
        int n = tour.length;

        for (int i = 0; i < n; i++) {
            int u = tour[i];
            int v = tour[(i + 1) % n];
            cost += d[u][v];
        }
        return cost;
    }

    public static void main(String[] args) throws Exception {
        Path csv = Paths.get("international_airports.csv");
        List<City> cities = loadAirports(csv);

        double[][] d = buildDistanceMatrix(cities);

        System.out.println("Distance matrix built for " + cities.size() + " airports.");
    }

    public static void main(String[] args) throws Exception {
        Path csv = Paths.get("international_airports.csv");
        List<City> airports = loadAirports(csv);

        System.out.println("Loaded " + airports.size() + " airports.");
    }
}
