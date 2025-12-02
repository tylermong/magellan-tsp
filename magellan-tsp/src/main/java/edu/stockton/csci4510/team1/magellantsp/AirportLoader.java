package edu.stockton.csci4510.team1.magellantsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AirportLoader {
  public static ArrayList<Airport> loadAirports(String filename) {
    ArrayList<Airport> airports = new ArrayList<>();
    InputStream is = AirportLoader.class.getResourceAsStream("/" + filename);
    String separator = ",";

    if (is == null) {
      System.err.println("Resource not found: " + filename);
      return airports;
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;

      // Skip header line
      br.readLine();

      // Parse each line into an Airport object
      while ((line = br.readLine()) != null) {
        String[] data = line.split(separator);

        if (data.length != 4) {
          System.err.println("Skipping malformed line: " + line);
          continue;
        }

        String country = data[0];
        String name = data[1];
        double lat = Double.parseDouble(data[2].trim());
        double lon = Double.parseDouble(data[3].trim());

        airports.add(new Airport(country, name, lat, lon));
      }
    } catch (IOException e) {
      System.err.println("Error loading airports: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.err.println("Error parsing airport data: " + e.getMessage());
    }

    return airports;
  }
}
