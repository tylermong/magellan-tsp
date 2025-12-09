package edu.stockton.csci4510.simulatedAnnealing;

public class SimulatedAnnealingTSP {

    private static final int ITERATIONS = 200_000;
    private static final int RUNS = 5;

    static class City {
        final String name;
        final double latDeg;
        final double lonDeg;

        City(String name, double latDeg, double lonDeg) {
            this.name = name;
            this.latDeg = latDeg;
            this.lonDeg = lonDeg;
        }
    }

    public static void main(String[] args) throws Exception {
        ;
    }
}
