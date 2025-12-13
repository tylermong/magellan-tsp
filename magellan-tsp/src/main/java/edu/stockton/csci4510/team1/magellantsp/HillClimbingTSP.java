package edu.stockton.csci4510.team1.magellantsp;

import java.util.ArrayList;
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
import org.cicirello.search.problems.tsp.TSPEdgeDistance;

public class HillClimbingTSP {

  private final ArrayList<Airport> airports;

  public HillClimbingTSP(ArrayList<Airport> airports) {
    this.airports = airports;
  }

  public void runExperiment() {
    int n = airports.size();
    System.out.println("Running Hill Climbing TSP on " + n + " airports...\n");

    double[] xs = new double[n]; // longitude
    double[] ys = new double[n]; // latitude
    for (int i = 0; i < n; i++) {
      xs[i] = airports.get(i).getLongitude();
      ys[i] = airports.get(i).getLatitude();
    }

    TSPEdgeDistance haversine =
        new TSPEdgeDistance() {
          @Override
          public double distance(double x1, double y1, double x2, double y2) {
            return HaversineDistance.calculateDistance(y1, x1, y2, x2);
          }
        };
    // pulls the haversine distance to be used as the distance function for edge costs
    TSP.Double tsp = new TSP.Double(xs, ys, haversine);

    Configurator.configureRandomGenerator(213);
    PermutationInitializer init = new PermutationInitializer(n);
    int restarts = 1; // anything but 1 was taking too long locally, must change !!!!!

    // fd = first descent, st steepest descent
    SolutionCostPair<Permutation> fdRev =
        new FirstDescentHillClimber<>(tsp, new ReversalMutation(), init).optimize(restarts);
    SolutionCostPair<Permutation> fdIns =
        new FirstDescentHillClimber<>(tsp, new InsertionMutation(), init).optimize(restarts);
    SolutionCostPair<Permutation> fdSwap =
        new FirstDescentHillClimber<>(tsp, new SwapMutation(), init).optimize(restarts);
    SolutionCostPair<Permutation> stRev =
        new SteepestDescentHillClimber<>(tsp, new ReversalMutation(), init).optimize(restarts);
    SolutionCostPair<Permutation> stIns =
        new SteepestDescentHillClimber<>(tsp, new InsertionMutation(), init).optimize(restarts);
    SolutionCostPair<Permutation> stSwap =
        new SteepestDescentHillClimber<>(tsp, new SwapMutation(), init).optimize(restarts);

    printResult("First Descent / Reversal", fdRev);
    printResult("First Descent / Insertion", fdIns);
    printResult("First Descent / Swap", fdSwap);
    printResult("Steepest Descent / Reversal", stRev);
    printResult("Steepest Descent / Insertion", stIns);
    printResult("Steepest Descent / Swap", stSwap);
  }

  // used chatgpt to make a nice formatted output, will change if desired
  private void printResult(String name, SolutionCostPair<Permutation> sol) {
    double cost = ((Number) sol.getCost()).doubleValue();
    System.out.printf("%-30s: Cost = %.3f km%n", name, cost);
  }
}
