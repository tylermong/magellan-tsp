package edu.stockton.csci4510.team1.magellantsp;

import java.util.ArrayList;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.evo.FitnessFunction;
import org.cicirello.search.problems.Problem;

public class TSPProblem implements FitnessFunction.Double<Permutation> {
    private double[][] distMatrix;

    public TSPProblem(ArrayList<Airport> airports) {
        this.distMatrix = HaversineDistance.buildDistanceMatrix(airports);
    }

    @Override
    public double fitness(Permutation candidate) {
        double total = 0.0;
        int n = candidate.length();

        for (int i = 0; i < n; i++) {
            int curr = candidate.get(i);
            int next = candidate.get((i + 1) % n);
            total += distMatrix[curr][next];
        }

        return -total; // negative because we minimize distance
    }

    @Override
    public Problem<Permutation> getProblem() {
        return null;
    }
}
