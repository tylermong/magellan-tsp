package edu.stockton.csci4510.team1.magellantsp;

import edu.stockton.csci4510.team1.magellantsp.util.Airport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.operators.CrossoverOperator;
import org.cicirello.search.operators.UndoableMutationOperator;
import org.cicirello.search.operators.permutations.CycleCrossover;
import org.cicirello.search.operators.permutations.OrderCrossover;
import org.cicirello.search.operators.permutations.PartiallyMatchedCrossover;
import org.cicirello.search.operators.permutations.ThreeOptMutation;

public class CrossoverTSP {
  private final ArrayList<Airport> airports;
  private final double[][] distanceMatrix;
  private final Random rng = new Random();

  // Parameters
  private static final int POP_SIZE = 50;
  private static final int GENERATIONS = 100;
  private static final int TOURNAMENT_K = 3;
  private static final double ELITE_KEEP = 0.05; // keep top 5% elites
  private static final int RUNS_PER_OPERATOR = 20; // number of runs per xover
  private static final int THREE_OPT_ITERS = 1000; // Number of 3-opt mutation attempts per GA run

  public CrossoverTSP(ArrayList<Airport> airports, double[][] distanceMatrix) {
    this.airports = airports;
    this.distanceMatrix = distanceMatrix;
  }

  // Tournament selection
  private Permutation tournamentSelect(List<Permutation> pop, int k) {
    Permutation best = null;
    double bestCost = Double.POSITIVE_INFINITY;
    for (int i = 0; i < k; i++) {
      Permutation candidate = pop.get(rng.nextInt(pop.size()));
      double candidateCost = tourCost(candidate);
      if (candidateCost < bestCost) {
        best = candidate;
        bestCost = candidateCost;
      }
    }
    return best;
  }

  // Computes tour cost
  private double tourCost(Permutation tour) {
    double cost = 0.0;
    final int n = tour.length();
    for (int i = 0; i < n; i++) {
      int currentIndex = tour.get(i);
      int nextIndex = tour.get((i + 1) % n);
      cost += distanceMatrix[currentIndex][nextIndex];
    }
    return cost;
  }

  // Compare crossovers + 3-Opt
  public void runExperiment() {
    final int n = airports.size();

    final List<CrossoverOperator<Permutation>> crossovers =
        List.of(new OrderCrossover(), new PartiallyMatchedCrossover(), new CycleCrossover());

    final List<String> names = List.of("OX", "PMX", "CX");

    class Stats {
      double best = Double.POSITIVE_INFINITY;
      double worst = Double.NEGATIVE_INFINITY;
    }

    System.out.println(
        "=== Crossover Comparison (best/worst over " + RUNS_PER_OPERATOR + " runs each) ===");
    System.out.printf("%-30s %15s %15s%n", "Operator", "Best", "Worst");

    for (int xoIndex = 0; xoIndex < crossovers.size(); xoIndex++) {
      String name = names.get(xoIndex);
      CrossoverOperator<Permutation> xo = crossovers.get(xoIndex);

      Stats s = new Stats(); // GA only
      Stats s3 = new Stats(); // GA + 3-Opt refinement

      UndoableMutationOperator<Permutation> threeOpt = new ThreeOptMutation();

      for (int runIndex = 0; runIndex < RUNS_PER_OPERATOR; runIndex++) {
        // Initial population
        List<Permutation> population = new ArrayList<>(POP_SIZE);
        RandomGenerator rg = rng;
        for (int i = 0; i < POP_SIZE; i++) {
          population.add(new Permutation(n, rg));
        }

        // Evolution loop
        for (int gen = 0; gen < GENERATIONS; gen++) {
          final int eliteCount = Math.max(1, (int) Math.round(population.size() * ELITE_KEEP));
          population.sort(Comparator.comparingDouble(this::tourCost));
          List<Permutation> next = new ArrayList<>(population.size());

          // keep elites
          for (int e = 0; e < eliteCount; e++) {
            next.add(new Permutation(population.get(e)));
          }

          // crossover fill
          while (next.size() < POP_SIZE) {
            Permutation p1 = tournamentSelect(population, TOURNAMENT_K);
            Permutation p2 = tournamentSelect(population, TOURNAMENT_K);
            Permutation c1 = new Permutation(p1);
            Permutation c2 = new Permutation(p2);
            xo.cross(c1, c2);
            if (next.size() < POP_SIZE - 1) {
              next.add(c1);
              next.add(c2);
            } else {
              next.add(tourCost(c1) <= tourCost(c2) ? c1 : c2);
            }
          }
          population = next;
        }

        // Best after GA
        Permutation bestGA =
            population.stream().min(Comparator.comparingDouble(this::tourCost)).orElse(null);

        double bestCost = (bestGA != null) ? tourCost(bestGA) : Double.POSITIVE_INFINITY;
        s.best = Math.min(s.best, bestCost);
        s.worst = Math.max(s.worst, bestCost);

        // Apply 3-Opt local search refinement to best GA solution
        double localCost = bestCost;
        if (bestGA != null && Double.isFinite(bestCost)) {
          Permutation refined = new Permutation(bestGA);
          for (int it = 0; it < THREE_OPT_ITERS; it++) {
            threeOpt.mutate(refined);
            double costNew = tourCost(refined);
            if (costNew <= localCost) {
              localCost = costNew;
            } else {
              threeOpt.undo(refined);
            }
          }
        }
        s3.best = Math.min(s3.best, localCost);
        s3.worst = Math.max(s3.worst, localCost);
      }

      System.out.printf("%-30s %15.3f %15.3f%n", name, s.best, s.worst);
      System.out.printf("%-30s %15.3f %15.3f%n", name + " + 3-Opt", s3.best, s3.worst);
    }
  }
}
