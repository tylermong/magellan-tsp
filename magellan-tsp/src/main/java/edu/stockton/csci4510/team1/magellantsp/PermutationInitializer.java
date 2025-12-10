package edu.stockton.csci4510.team1.magellantsp;

import org.cicirello.permutations.Permutation;
import org.cicirello.search.operators.Initializer;

public class PermutationInitializer implements Initializer<Permutation> {
    private int len;

    public PermutationInitializer(int length) {
        this.len = length;
    }

    @Override
    public Permutation createCandidateSolution() {
        return new Permutation(len);
    }

    @Override
    public PermutationInitializer split() {
        return new PermutationInitializer(len);
    }
}
