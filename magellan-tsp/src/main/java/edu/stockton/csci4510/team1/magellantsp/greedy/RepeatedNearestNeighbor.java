package edu.stockton.csci4510.team1.magellantsp.greedy;

public class RepeatedNearestNeighbor {
  public int[] solve(double[][] distanceMatrix) {
    if (distanceMatrix == null || distanceMatrix.length == 0) {
      throw new IllegalArgumentException("Distance matrix cannot be null or empty");
    }
    int n = distanceMatrix.length;
    NearestNeighborSolver solver = new NearestNeighborSolver();

    int[] bestTour = null;
    double bestCost = Double.MAX_VALUE;

    // Loop through every airport as a starting point
    for (int startNode = 0; startNode < n; startNode++) {

      // Solve the TSP starting from the current airport
      int[] currentTour = solver.solve(startNode, distanceMatrix);

      // Calculate the cost of the current tour
      double currentCost = calculateTourCost(currentTour, distanceMatrix);

      // Update the best tour if the current one is better
      if (currentCost < bestCost) {
        bestCost = currentCost;
        bestTour = currentTour;
        System.out.println("New best found starting at node " + startNode + ": " + bestCost);
      }
    }

    return bestTour;
  }

  public static double calculateTourCost(int[] tour, double[][] distanceMatrix) {
    double cost = 0.0;

    // Sum the distances between each consecutive city in the tour
    for (int i = 0; i < tour.length - 1; i++) {
      int from = tour[i];
      int to = tour[i + 1];
      cost += distanceMatrix[from][to];
    }

    // Add the distance from the last city back to the first city to complete the tour
    int lastCity = tour[tour.length - 1];
    int firstCity = tour[0];
    cost += distanceMatrix[lastCity][firstCity];

    return cost;
  }
}
