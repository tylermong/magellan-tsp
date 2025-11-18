package edu.stockton.csci4510.team1.magellantsp;

import java.io.*;
import java.util.*;

public class Main {
  List<Airport> airports = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    List<Airport> airports = importAirports();
    crossoverTSP(airports);
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

  public static void crossoverTSP(List<Airport> airports) {
    // add code here
    CrossoverTSP crossover = new CrossoverTSP(new ArrayList<>(airports));
    crossover.runExperiment();
  }

  public void tylerTSP() {
    // add code here
  }

  public static List<Airport> importAirports() throws IOException {
    List<Airport> airports = new ArrayList<>();

    InputStream is = Main.class.getClassLoader().getResourceAsStream("international_airports.csv");

    BufferedReader br = new BufferedReader(new InputStreamReader(is));

    // Skip header row
    br.readLine();

    String line;
    while ((line = br.readLine()) != null) {
      String[] parts = line.split(",");
      if (parts.length == 4) {
        String country = parts[0].trim();
        String airportName = parts[1].trim();
        double latitude = Double.parseDouble(parts[2].trim());
        double longitude = Double.parseDouble(parts[3].trim());
        Airport airport = new Airport(country, airportName, latitude, longitude);
        airports.add(airport);
      }
    }

    return airports;
  }
}
