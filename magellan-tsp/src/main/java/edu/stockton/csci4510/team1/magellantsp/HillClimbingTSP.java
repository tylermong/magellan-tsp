package edu.stockton.csci4510.team1.magellantsp;

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
  private final double[][] distanceMatrix;

  public HillClimbingTSP(double[][] distanceMatrix) {
    this.distanceMatrix = distanceMatrix;
  }

  public void runExperiment() {
    int n = distanceMatrix.length;
    System.out.println("Running Hill Climbing TSP on " + n + " airports...\n");

    double[] indices = new double[n];
    for (int i = 0; i < n; i++) {
      indices[i] = i;
    }

    TSPEdgeDistance matrixLookup =
        new TSPEdgeDistance() {
          @Override
          public double distance(double i, double dummy1, double j, double dummy2) {
            return distanceMatrix[(int) i][(int) j];
          }
        };

    TSP.Double tsp = new TSP.Double(indices, new double[n], matrixLookup);

    Configurator.configureRandomGenerator(213);
    PermutationInitializer init = new PermutationInitializer(n);
    int restarts = 10; // FIX: from 1 to 10

    // fd = first descent, st steepest descent
    int totalSteps = 6;
    int currentStep = 1;

    System.out.println(
        "[" + currentStep + "/" + totalSteps + "] Running First Descent / Reversal...");
    SolutionCostPair<Permutation> fdRev =
        new FirstDescentHillClimber<>(tsp, new ReversalMutation(), init).optimize(restarts);
    printResult("First Descent / Reversal", fdRev);
    currentStep++;

    System.out.println(
        "[" + currentStep + "/" + totalSteps + "] Running First Descent / Insertion...");
    SolutionCostPair<Permutation> fdIns =
        new FirstDescentHillClimber<>(tsp, new InsertionMutation(), init).optimize(restarts);
    printResult("First Descent / Insertion", fdIns);
    currentStep++;

    System.out.println("[" + currentStep + "/" + totalSteps + "] Running First Descent / Swap...");
    SolutionCostPair<Permutation> fdSwap =
        new FirstDescentHillClimber<>(tsp, new SwapMutation(), init).optimize(restarts);
    printResult("First Descent / Swap", fdSwap);
    currentStep++;

    System.out.println(
        "[" + currentStep + "/" + totalSteps + "] Running Steepest Descent / Reversal...");
    SolutionCostPair<Permutation> stRev =
        new SteepestDescentHillClimber<>(tsp, new ReversalMutation(), init).optimize(restarts);
    printResult("Steepest Descent / Reversal", stRev);
    currentStep++;

    System.out.println(
        "[" + currentStep + "/" + totalSteps + "] Running Steepest Descent / Insertion...");
    SolutionCostPair<Permutation> stIns =
        new SteepestDescentHillClimber<>(tsp, new InsertionMutation(), init).optimize(restarts);
    printResult("Steepest Descent / Insertion", stIns);
    currentStep++;

    System.out.println(
        "[" + currentStep + "/" + totalSteps + "] Running Steepest Descent / Swap...");
    SolutionCostPair<Permutation> stSwap =
        new SteepestDescentHillClimber<>(tsp, new SwapMutation(), init).optimize(restarts);
    printResult("Steepest Descent / Swap", stSwap);
  }

  private void printResult(String name, SolutionCostPair<Permutation> sol) {
    double cost = ((Number) sol.getCost()).doubleValue();
    System.out.printf("%-30s: Cost = %.3f km%n", name, cost);
  }
}
