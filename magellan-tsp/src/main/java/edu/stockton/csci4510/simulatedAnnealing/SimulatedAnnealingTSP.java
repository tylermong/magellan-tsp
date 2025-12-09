package edu.stockton.csci4510.simulatedAnnealing;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

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

    public static void main(String[] args) throws Exception {
        Path csv = Paths.get("international_airports.csv");
        List<City> airports = loadAirports(csv);

        System.out.println("Loaded " + airports.size() + " airports.");
    }
}
