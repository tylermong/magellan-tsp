package edu.stockton.csci4510.team1.magellantsp.genevo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.operators.CrossoverOperator;
import org.cicirello.search.operators.permutations.CycleCrossover;
import org.cicirello.search.operators.permutations.OrderCrossover;
import org.cicirello.search.operators.permutations.PartiallyMatchedCrossover;
import org.cicirello.search.operators.permutations.SwapMutation;

public class GenEvoComputation {
  private ArrayList<Airport> airports;
  private TSPProblem problem;
  private Random rng = new Random();

  private static final int POP_SIZE = 50;
  private static final int GENS = 100;
  private static final double MUT_RATE = 0.3;
  private static final int TOURNAMENT_K = 3;
  private static final int ELITES = 2;
  private static final int RUNS = 20;

  public GenEvoComputation(ArrayList<Airport> airports, double[][] distanceMatrix) {
    this.airports = airports;
    this.problem = new TSPProblem(distanceMatrix);
  }

  private double tourCost(Permutation tour) {
    return -problem.fitness(tour);
  }

  private Permutation tournamentSelect(List<Permutation> pop, int k) {
    Permutation best = null;
    double bestFit = Double.POSITIVE_INFINITY;
    for (int i = 0; i < k; i++) {
      Permutation p = pop.get(rng.nextInt(pop.size()));
      double fit = tourCost(p);
      if (fit < bestFit) {
        best = p;
        bestFit = fit;
      }
    }
    return best;
  }

  public void runExperiment() {
    int n = airports.size();

    System.out.println(
        "Generational Evolutionary Computation using Chips-n-Salsa Operators (best/worst over "
            + RUNS
            + " runs each) ");
    System.out.printf("%-28s %12s %12s%n", "Operator", "Best", "Worst");

    var crossovers =
        new Object[][] {
          {"Order Crossover (OX)", new OrderCrossover()},
          {"Partially Matched (PMX)", new PartiallyMatchedCrossover()},
          {"Cycle Crossover (CX)", new CycleCrossover()}
        };

    for (Object[] entry : crossovers) {
      String name = (String) entry[0];
      @SuppressWarnings("unchecked")
      var xover = (CrossoverOperator<Permutation>) entry[1];

      double best = Double.POSITIVE_INFINITY;
      double worst = Double.NEGATIVE_INFINITY;

      for (int run = 0; run < RUNS; run++) {
        List<Permutation> pop = new ArrayList<>(POP_SIZE);
        for (int i = 0; i < POP_SIZE; i++) {
          pop.add(new Permutation(n, rng));
        }

        SwapMutation mut = new SwapMutation();

        for (int gen = 0; gen < GENS; gen++) {
          pop.sort(Comparator.comparingDouble(this::tourCost));

          List<Permutation> nextGen = new ArrayList<>(POP_SIZE);

          // keep elites
          for (int i = 0; i < ELITES; i++) {
            nextGen.add(new Permutation(pop.get(i)));
          }

          while (nextGen.size() < POP_SIZE) {
            Permutation p1 = tournamentSelect(pop, TOURNAMENT_K);
            Permutation p2 = tournamentSelect(pop, TOURNAMENT_K);

            Permutation c1 = new Permutation(p1);
            Permutation c2 = new Permutation(p2);

            xover.cross(c1, c2);

            if (rng.nextDouble() < MUT_RATE) {
              mut.mutate(c1);
            }
            if (rng.nextDouble() < MUT_RATE) {
              mut.mutate(c2);
            }

            if (nextGen.size() < POP_SIZE - 1) {
              nextGen.add(c1);
              nextGen.add(c2);
            } else {
              nextGen.add(tourCost(c1) <= tourCost(c2) ? c1 : c2);
            }
          }

          pop = nextGen;
        }

        Permutation bestTour =
            pop.stream().min(Comparator.comparingDouble(this::tourCost)).orElse(null);
        double dist = (bestTour != null) ? tourCost(bestTour) : Double.POSITIVE_INFINITY;

        best = Math.min(best, dist);
        worst = Math.max(worst, dist);
      }

      System.out.printf("%-28s %12.3f %12.3f%n", name, best, worst);
    }
  }
}
