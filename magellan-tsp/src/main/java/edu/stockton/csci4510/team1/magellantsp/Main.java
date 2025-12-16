package edu.stockton.csci4510.team1.magellantsp;

import java.io.*;
import java.util.*;

public class Main {
  List<Airport> airports = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ArrayList<Airport> airports = AirportLoader.loadAirports("international_airports.csv");
    double[][] distanceMatrix = HaversineDistance.buildDistanceMatrix(airports);
    localSearchTSP(airports, distanceMatrix);
    crossoverTSP(airports, distanceMatrix);
  }

  public static void localSearchTSP(ArrayList<Airport> airports, double[][] distanceMatrix) {
    HillClimbingTSP hc = new HillClimbingTSP(distanceMatrix);
    hc.runExperiment();
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

  public void tylerTSP() {
    // add code here
  }
}
