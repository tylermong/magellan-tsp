package edu.stockton.csci4510.team1.magellantsp;

import edu.stockton.csci4510.team1.magellantsp.greedy.GreedyTester;
import edu.stockton.csci4510.team1.magellantsp.simulatedannealing.InsertionMutationTSP;
import edu.stockton.csci4510.team1.magellantsp.simulatedannealing.SimulatedAnnealingTSP;
import edu.stockton.csci4510.team1.magellantsp.util.Airport;
import edu.stockton.csci4510.team1.magellantsp.util.AirportLoader;
import edu.stockton.csci4510.team1.magellantsp.util.HaversineDistance;
import java.io.*;
import java.util.*;

public class Main {
  List<Airport> airports = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ArrayList<Airport> airports = AirportLoader.loadAirports("international_airports.csv");
    double[][] distanceMatrix = HaversineDistance.buildDistanceMatrix(airports);
    localSearchTSP(airports, distanceMatrix);
    SimulatedAnnealingTSP(airports, distanceMatrix);
    InsertionMutationTSP(airports, distanceMatrix);
    crossoverTSP(airports, distanceMatrix);
    greedyTSP(airports, distanceMatrix);
  }

  public static void localSearchTSP(ArrayList<Airport> airports, double[][] distanceMatrix) {
    HillClimbingTSP hc = new HillClimbingTSP(distanceMatrix);
    hc.runExperiment();
  }

  public static void SimulatedAnnealingTSP(ArrayList<Airport> airports, double[][] distanceMatrix) {
    List<SimulatedAnnealingTSP.City> cities = new ArrayList<>();
    for (Airport a : airports) {
      cities.add(
          new SimulatedAnnealingTSP.City(a.getAirportName(), a.getLatitude(), a.getLongitude()));
    }

    SimulatedAnnealingTSP sa = new SimulatedAnnealingTSP(cities, distanceMatrix, 200_000, 5);
    sa.runAll();
  }

  public static void InsertionMutationTSP(ArrayList<Airport> airports, double[][] distanceMatrix) {
    List<InsertionMutationTSP.City> cities = new ArrayList<>();
    for (Airport a : airports) {
      cities.add(
          new InsertionMutationTSP.City(a.getAirportName(), a.getLatitude(), a.getLongitude()));
    }

    InsertionMutationTSP im = new InsertionMutationTSP(cities, distanceMatrix, 200_000, 5);
    im.runAll();
  }

  public static void crossoverTSP(List<Airport> airports, double[][] distanceMatrix) {
    CrossoverTSP crossover = new CrossoverTSP(new ArrayList<>(airports), distanceMatrix);
    crossover.runExperiment();
  }

  public static void greedyTSP(List<Airport> airports, double[][] distanceMatrix) {
    GreedyTester tester = new GreedyTester(airports, distanceMatrix);
    tester.runTest();
  }
}
