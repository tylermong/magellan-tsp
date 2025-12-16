package edu.stockton.csci4510.team1.magellantsp;

import edu.stockton.csci4510.team1.magellantsp.greedy.GreedyTester;
import edu.stockton.csci4510.simulatedAnnealing.SimulatedAnnealingTSP;
import edu.stockton.csci4510.simulatedAnnealing.InsertionMutationTSP;
import java.io.*;
import java.util.*;

public class Main {
  List<Airport> airports = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ArrayList<Airport> airports = AirportLoader.loadAirports("international_airports.csv");
    double[][] distanceMatrix = HaversineDistance.buildDistanceMatrix(airports);
    SimulatedAnnealingTSP(airports, distanceMatrix);
    InsertionMutationTSP(airports, distanceMatrix);
    localSearchTSP(airports, distanceMatrix);
    crossoverTSP(airports, distanceMatrix);
    greedyTSP(airports, distanceMatrix);
    SimulatedAnnealingTSP(airports, distanceMatrix);
    InsertionMutationTSP(airports, distanceMatrix);
  }

  public static void localSearchTSP(ArrayList<Airport> airports, double[][] distanceMatrix) {
    HillClimbingTSP hc = new HillClimbingTSP(distanceMatrix);
    hc.runExperiment();
  }


  public static void SimulatedAnnealingTSP(ArrayList<Airport> airports,
                                           double[][] distanceMatrix) {

    List<SimulatedAnnealingTSP.City> cities = new ArrayList<>();
    for (Airport a : airports) {
      cities.add(new SimulatedAnnealingTSP.City(
          a.getName(), a.getLatitude(), a.getLongitude()));
    }

    SimulatedAnnealingTSP sa =
        new SimulatedAnnealingTSP(cities, distanceMatrix, 200_000, 5);

    sa.runAll();
  }

  public static void InsertionMutationTSP(ArrayList<Airport> airports,
                                          double[][] distanceMatrix) {

    List<InsertionMutationTSP.City> cities = new ArrayList<>();
    for (Airport a : airports) {
      cities.add(new InsertionMutationTSP.City(
          a.getName(), a.getLatitude(), a.getLongitude()));
    }

    InsertionMutationTSP im =
        new InsertionMutationTSP(cities, distanceMatrix, 200_000, 5);

    im.runAll();
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
