package edu.stockton.csci4510.team1.magellantsp;

import java.io.*;
import java.util.*;

public class Main {
  List<Airport> airports = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ArrayList<Airport> airports = AirportLoader.loadAirports("international_airports.csv");
    // double[][] dMatrix = HaversineDistance.buildDistanceMatrix(airports);
    localSearchTSP(airports);
    // crossoverTSP(airports, dMatrix);
  }

  public static void localSearchTSP(ArrayList<Airport> airports) {
    HillClimbingTSP hc = new HillClimbingTSP(airports);
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
  }

  public void tylerTSP() {
    // add code here
  }
}
