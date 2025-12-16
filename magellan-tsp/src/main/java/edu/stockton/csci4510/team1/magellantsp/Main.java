package edu.stockton.csci4510.team1.magellantsp;

import edu.stockton.csci4510.team1.magellantsp.greedy.GreedyTester;
import java.io.*;
import java.util.*;

public class Main {
  List<Airport> airports = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ArrayList<Airport> airports = AirportLoader.loadAirports("international_airports.csv");
    double[][] dMatrix = HaversineDistance.buildDistanceMatrix(airports);

    crossoverTSP(airports, dMatrix);
    greedyTSP(airports, dMatrix);
  }

  public void localSearchTSP() {
    // add info here
  }

  public void simmulatedAnnealingTSP() {
    // add code here
  }

  public void mutationTSP() {
    // add code here
  }

  public static void crossoverTSP(List<Airport> airports, double[][] distanceMatrix) {
    // add code here
    CrossoverTSP crossover = new CrossoverTSP(new ArrayList<>(airports), distanceMatrix);
    crossover.runExperiment();
  }

  public static void greedyTSP(List<Airport> airports, double[][] distanceMatrix) {
    GreedyTester tester = new GreedyTester(airports, distanceMatrix);
    tester.runTest();
  }
}
