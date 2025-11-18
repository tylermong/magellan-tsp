package edu.stockton.csci4510.team1.magellantsp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.operators.CrossoverOperator;
import org.cicirello.search.operators.permutations.CycleCrossover;
import org.cicirello.search.operators.permutations.OrderCrossover;
import org.cicirello.search.operators.permutations.PartiallyMatchedCrossover;

public class CrossoverTSP {

  private final ArrayList<Airport> airports;
  private final double[][] distanceMatrix;
  private final Random rng = new Random();

  //Parameters can be changed to see different results
  private static final int POP_SIZE = 50;
  private static final int GENERATIONS = 100;
  private static final int TOURNAMENT_K = 3;
  private static final double ELITE_KEEP = 0.05; // keep top 5% elites
  private static final int RUNS_PER_OPERATOR = 20; // number of runs for each crossover type

  public CrossoverTSP(ArrayList<Airport> airports) {
    this.airports = airports;
    this.distanceMatrix = HaversineDistance.buildDistanceMatrix(airports);
  }


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

  //Computes length for a permutation of the matrix.
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

  //Compare crossovers over multiple runs
  public void runExperiment() {
    final int n = airports.size();

    final List<CrossoverOperator<Permutation>> crossovers =
        List.of(new OrderCrossover(), new PartiallyMatchedCrossover(), new CycleCrossover());
    final List<String> names =
        List.of("Order Crossover (OX)", "Partially Matched (PMX)", "Cycle Crossover (CX)");

    class Stats {
      double best = Double.POSITIVE_INFINITY;
      double worst = Double.NEGATIVE_INFINITY;
      int runs = 0;
    }

    System.out.println(
        "=== Crossover Comparison (best/worst over " + RUNS_PER_OPERATOR + " runs each) ===");
    System.out.printf("%-28s %12s %12s%n", "Operator", "Best", "Worst");

    for (int xoIndex = 0; xoIndex < crossovers.size(); xoIndex++) {
      String name = names.get(xoIndex);
      CrossoverOperator<Permutation> xo = crossovers.get(xoIndex);
      Stats s = new Stats();
      for (int runIndex = 0; runIndex < RUNS_PER_OPERATOR; runIndex++) {
        // --- Initialize population
        List<Permutation> population = new ArrayList<>(POP_SIZE);
        RandomGenerator rg = rng;
        for (int popIndex = 0; popIndex < POP_SIZE; popIndex++) {
          population.add(new Permutation(n, rg));
        }
        // --- Evolutionary loop (generational with elitism)
        for (int gen = 0; gen < GENERATIONS; gen++) {
          final int eliteCount = Math.max(1, (int) Math.round(population.size() * ELITE_KEEP));
          population.sort(Comparator.comparingDouble(this::tourCost));
          List<Permutation> next = new ArrayList<>(population.size());
          for (int eliteIndex = 0; eliteIndex < eliteCount; eliteIndex++) {
            next.add(new Permutation(population.get(eliteIndex)));
          }
          while (next.size() < POP_SIZE) {
            Permutation parentOne = tournamentSelect(population, TOURNAMENT_K);
            Permutation parentTwo = tournamentSelect(population, TOURNAMENT_K);
            Permutation childOne = new Permutation(parentOne);
            Permutation childTwo = new Permutation(parentTwo);
            xo.cross(childOne, childTwo);
            if (next.size() < POP_SIZE - 1) {
              next.add(childOne);
              next.add(childTwo);
            } else {
              next.add(tourCost(childOne) <= tourCost(childTwo) ? childOne : childTwo);
            }
          }
          population = next;
        }
        Permutation best =
            population.stream().min(Comparator.comparingDouble(this::tourCost)).orElse(null);
        double bestCost = (best != null) ? tourCost(best) : Double.POSITIVE_INFINITY;
        s.best = Math.min(s.best, bestCost);
        s.worst = Math.max(s.worst, bestCost);
        s.runs++;
      }
      System.out.printf("%-28s %12.3f %12.3f%n", name, s.best, s.worst);
    }
  }
}
