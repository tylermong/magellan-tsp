package edu.stockton.csci4510.team1.magellantsp;

import java.util.ArrayList;

public class HaversineDistance {
    private static final double EARTH_RADIUS_KM = 6371.0;

    // Calculate distance between two airports in kilometers
    public static double calculateDistance(Airport airport1, Airport airport2) {
        return calculateDistance(
                airport1.getLatitude(),
                airport1.getLongitude(),
                airport2.getLatitude(),
                airport2.getLongitude());
    }

    // Calculate distance between two coordinates using Haversine formula
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double sinDLat = Math.sin(dLat / 2);
        double sinDLon = Math.sin(dLon / 2);
        
        double a = sinDLat * sinDLat + Math.cos(lat1Rad) * Math.cos(lat2Rad) * sinDLon * sinDLon;

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    // Build distance matrix for all airport pairs
    public static double[][] buildDistanceMatrix(ArrayList<Airport> airports) {
        int n = airports.size();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) { // Start j from i
              if (i == j) {
                matrix[i][j] = 0.0;
              } else {
                double distance = calculateDistance(airports.get(i), airports.get(j));
                matrix[i][j] = matrix[j][i] = distance; // Mirror the value
              }
            }
          }
          
        return matrix;
    }
    
}
