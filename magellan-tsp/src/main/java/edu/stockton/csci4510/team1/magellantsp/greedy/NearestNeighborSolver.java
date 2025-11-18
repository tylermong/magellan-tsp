public class NearestNeighborSolver {
  public int[] solve(int startNode, double[][] distanceMatrix) {
    int n = distanceMatrix.length;
    int[] tour = new int[n];
    boolean[] visited = new boolean[n];

    // Initialize the tour with the start node
    int currentNode = startNode;
    tour[0] = currentNode;
    visited[currentNode] = true;

    // Iterate through all nodes to build the tour
    for (int i = 1; i < n; i++) {
      int nextNode = -1;
      double shortestDistance = Double.MAX_VALUE;

      // Find the nearest unvisited node
      for (int candidate = 0; candidate < n; candidate++) {
        if (!visited[candidate] && distanceMatrix[currentNode][candidate] < shortestDistance) {
          nextNode = candidate;
          shortestDistance = distanceMatrix[currentNode][candidate];
        }
      }

      // Travel to the found nearest node
      tour[i] = nextNode;
      visited[nextNode] = true;
      currentNode = nextNode;
    }

    return tour;
  }
}
