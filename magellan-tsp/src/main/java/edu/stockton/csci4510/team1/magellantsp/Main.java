package edu.stockton.csci4510.team1.magellantsp;

import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {
    ArrayList<Airport> airports = AirportLoader.loadAirports("international_airports.csv");

    HillClimbingTSP hc = new HillClimbingTSP(airports);
    hc.runExperiment();
  }
}
