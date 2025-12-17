package edu.stockton.csci4510.team1.magellantsp.simulatedannealing;

import java.util.*;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.ProgressTracker;
import org.cicirello.search.SolutionCostPair;
import org.cicirello.search.operators.Initializer;
import org.cicirello.search.operators.UndoableMutationOperator;
import org.cicirello.search.operators.permutations.PermutationInitializer;
import org.cicirello.search.operators.permutations.TwoChangeMutation;
import org.cicirello.search.problems.OptimizationProblem;
import org.cicirello.search.sa.SimulatedAnnealing;

public class SimulatedAnnealingTSP {

  // number of iterations per simulated annealing run
  private final int ITERATIONS;

  // number of independent runs
  private final int RUNS;

  // container for airport data
  public static class City {
    public final double latDeg;
    public final double lonDeg;
  
    public City(double latDeg, double lonDeg) {
      this.latDeg = latDeg;
      this.lonDeg = lonDeg;
    }
  }
  

  // store airport data
  private final List<City> cities;

  // distance matrix
  private final double[][] d;

  // wrap tsp as an optimization problem for chips-n-salsa
  static class TSPProblem implements OptimizationProblem<Permutation> {
    private final double[][] d;

    TSPProblem(double[][] d) {
      this.d = d;
    }

    @Override
    public double cost(Permutation tour) {
      return tourCost(tour, d);
    }

    @Override
    public double value(Permutation tour) {
      return tourCost(tour, d);
    }

    @Override
    public double minCost() {
      return 0.0;
    }
  }

  // store summary data for each run
  static class RunSummary {
    final int runNumber;
    final double costKm;
    final double seconds;
    final int firstIdx;
    final int lastIdx;

    RunSummary(int runNumber, double costKm, double seconds, int firstIdx, int lastIdx) {
      this.runNumber = runNumber;
      this.costKm = costKm;
      this.seconds = seconds;
      this.firstIdx = firstIdx;
      this.lastIdx = lastIdx;
    }
  }

  // constructor expects airport list and distance matrix from main
  public SimulatedAnnealingTSP(
      List<City> cities, double[][] distanceMatrix, int iterations, int runs) {

    this.cities = cities;
    this.d = distanceMatrix;
    this.ITERATIONS = iterations;
    this.RUNS = runs;
  }

  // run simulated annealing multiple times
  public List<RunSummary> runAll() {
    int n = cities.size();

    System.out.println("Loaded " + n + " airports.");
    System.out.println("Iterations per run: " + String.format("%,d", ITERATIONS));
    System.out.println("Total runs: " + RUNS);
    System.out.println();

    // create tsp optimization problem
    OptimizationProblem<Permutation> problem = new TSPProblem(d);

    // store results from all runs
    List<RunSummary> summaries = new ArrayList<>();

    // run simulated annealing multiple times
    for (int run = 1; run <= RUNS; run++) {
      Initializer<Permutation> init = new PermutationInitializer(n);

      // swap two positions in the tour
      UndoableMutationOperator<Permutation> move = new TwoChangeMutation();

      // track best solution during the run
      ProgressTracker<Permutation> tracker = new ProgressTracker<>();

      SimulatedAnnealing<Permutation> sa = new SimulatedAnnealing<>(problem, move, init, tracker);

      // time this run
      long startNano = System.nanoTime();
      SolutionCostPair<Permutation> best = sa.optimize(ITERATIONS);
      long endNano = System.nanoTime();

      double elapsedSeconds = (endNano - startNano) / 1_000_000_000.0;

      double bestCost = best.getCostDouble();
      Permutation bestTour = best.getSolution();

      int firstIdx = bestTour.get(0);
      int lastIdx = bestTour.get(n - 1);

      summaries.add(new RunSummary(run, bestCost, elapsedSeconds, firstIdx, lastIdx));

      System.out.printf(
          "Run %d finished: cost = %.3f km, time = %.3f s%n", run, bestCost, elapsedSeconds);
    }

    // sort runs by tour cost
    summaries.sort(Comparator.comparingDouble(r -> r.costKm));

    System.out.println();
    System.out.println("===== Top " + RUNS + " runs by tour cost =====");

    // print summary for each run
    for (int rank = 0; rank < summaries.size(); rank++) {
      RunSummary r = summaries.get(rank);

      System.out.printf(
          "#%d  (Run %d)%n" +
              "  Cost: %.3f km%n" +
              "  Time: %.3f s%n" +
              "  Start index: %d%n" +
              "  End index:   %d%n%n",
          rank + 1,
          r.runNumber,
          r.costKm,
          r.seconds,
          r.firstIdx,
          r.lastIdx);
    }

    return summaries;
  }

  // compute total tour cost
  static double tourCost(Permutation tour, double[][] d) {
    double cost = 0.0;
    int n = tour.length();

    for (int i = 0; i < n; i++) {
      int u = tour.get(i);
      int v = tour.get((i + 1) % n); // wrap to close the tour
      cost += d[u][v];
    }
    return cost;
  }
}
