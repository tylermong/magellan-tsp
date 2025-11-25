package edu.stockton.csci4510.team1.magellantsp.greedy;

import edu.stockton.csci4510.team1.magellantsp.Airport;
import edu.stockton.csci4510.team1.magellantsp.AirportLoader;
import edu.stockton.csci4510.team1.magellantsp.HaversineDistance;
import java.util.ArrayList;

public class GreedyTester {
  public static void runTest() {
    System.out.println("=== Testing Repeated Nearest Neighbor (RNN) ===");

    // Load data from src/main/resources
    String filename = "international_airports.csv";
    ArrayList<Airport> airports = AirportLoader.loadAirports(filename);

    if (airports.isEmpty()) {
      System.err.println("Could not load any airports. Check file path/contents.");
      return;
    }
    System.out.println("Successfully loaded " + airports.size() + " airports.");

    // Build distance matrix
    System.out.println("Building Distance Matrix...");
    double[][] matrix = HaversineDistance.buildDistanceMatrix(airports);

    // Execute Algorithm
    System.out.println("Running RNN...");
    RepeatedNearestNeighbor solver = new RepeatedNearestNeighbor();

    long startTime = System.currentTimeMillis();
    int[] bestTourIndices = solver.solve(matrix);
    if (bestTourIndices == null) {
      System.err.println("Algorithm failed to find a valid tour.");
      return;
    }
    long endTime = System.currentTimeMillis();

    // Print results
    printTourDetails(bestTourIndices, airports, matrix, endTime - startTime);
    System.out.println("=========================================================");
  }

  private static void printTourDetails(
      int[] tour, ArrayList<Airport> airports, double[][] matrix, long timeMs) {

    RepeatedNearestNeighbor costCalculator = new RepeatedNearestNeighbor();
    double totalDistance = costCalculator.calculateTourCost(tour, matrix);

    System.out.println("\n--- RNN Results ---");
    System.out.printf("Execution Time: %d ms\n", timeMs);
    System.out.println("Tour Order (First 5 Cities):");

    // Print tour path and calculate distance
    System.out.print("\tPath: ");
    for (int i = 0; i < tour.length; i++) {
      int current = tour[i];

      // Print label
      if (i < 5 || i > tour.length - 3) {
        System.out.print(airports.get(current).getAirportName());

        if (i < tour.length - 1) {
          System.out.print(" -> ");
        }
      } else if (i == 5) {
        System.out.print("...[" + (tour.length - 7) + " more cities]... -> ");
      }
    }

    // Final return to start
    System.out.println(" -> " + airports.get(tour[0]).getAirportName());

    System.out.printf("Total Tour Distance: %.2f km\n", totalDistance);
  }
}
