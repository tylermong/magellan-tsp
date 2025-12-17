package edu.stockton.csci4510.team1.magellantsp.greedy;

import edu.stockton.csci4510.team1.magellantsp.util.Airport;
import java.util.List;

public class GreedyTester {
  private List<Airport> airports;
  private double[][] matrix;

  public GreedyTester(List<Airport> airports, double[][] matrix) {
    this.airports = airports;
    this.matrix = matrix;
  }

  public void runTest() {
    System.out.println("\n=== Testing Repeated Nearest Neighbor (RNN) ===");

    if (airports == null || airports.isEmpty()) {
      System.err.println("Invalid airports list provided.");
      return;
    }
    if (matrix == null || matrix.length == 0) {
      System.err.println("Invalid distance matrix provided.");
      return;
    }

    // Execute Algorithm
    RepeatedNearestNeighbor solver = new RepeatedNearestNeighbor();

    long startTime = System.currentTimeMillis();
    int[] bestTourIndices = solver.solve(matrix);
    if (bestTourIndices == null) {
      System.err.println("Algorithm failed to find a valid tour.");
      return;
    }
    long endTime = System.currentTimeMillis();

    // Print results
    printTourDetails(bestTourIndices, endTime - startTime);
    System.out.println("=========================================================");
  }

  private void printTourDetails(int[] tour, long timeMs) {

    double totalDistance = RepeatedNearestNeighbor.calculateTourCost(tour, matrix);

    System.out.printf("Execution Time: %d ms\n", timeMs);
    System.out.println("Tour Order (Abridged):");

    // Print tour path and calculate distance
    System.out.print("\tPath: ");
    for (int i = 0; i < tour.length; i++) {
      int current = tour[i];

      // Print label
      if (i < 2 || i > tour.length - 2) {
        System.out.print(airports.get(current).getAirportName());

        if (i < tour.length - 1) {
          System.out.print(" -> ");
        }
      } else if (i == 2) {
        System.out.print("...[" + (tour.length - 4) + " more cities]... -> ");
      }
    }

    // Final return to start
    System.out.println(" -> " + airports.get(tour[0]).getAirportName());

    System.out.printf("Total Tour Distance: %.2f km\n", totalDistance);
  }
}
