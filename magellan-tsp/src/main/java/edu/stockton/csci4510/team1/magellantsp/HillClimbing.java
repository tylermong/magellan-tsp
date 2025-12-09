package edu.stockton.csci4510.team1.magellantsp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.cicirello.permutations.Permutation;
import org.cicirello.search.Configurator;
import org.cicirello.search.SolutionCostPair;
import org.cicirello.search.hc.FirstDescentHillClimber;
import org.cicirello.search.hc.SteepestDescentHillClimber;
import org.cicirello.search.operators.permutations.InsertionMutation;
import org.cicirello.search.operators.permutations.PermutationInitializer;
import org.cicirello.search.operators.permutations.ReversalMutation;
import org.cicirello.search.operators.permutations.SwapMutation;
import org.cicirello.search.problems.tsp.TSP;

public class HillClimbing {
  /**
   * Allows City objects to be made and used for our Travelling Sales Person Problem. Will store the
   * name, the latitude, and the longitude for each airport
   */
  static class City {
    final String name;
    final double lat, lon;

    City(String name, double lat, double lon) {
      this.name = name;
      this.lat = lat;
      this.lon = lon;
    }
  }

  /**
   * Will load the airports from the csv into their own objects and will put them into an arraylist
   *
   * @param csv to extract the lines to seperate into the variables we need
   * @return ArrayList list containing all city/airport objects
   */
  static List<City> loadAirports(InputStream csv) throws IOException {
    // FIX: InputStream must be read manually using BufferedReader
    BufferedReader reader = new BufferedReader(new InputStreamReader(csv, StandardCharsets.UTF_8));

    // gathers all the lines to process
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }

    if (lines.isEmpty()) throw new IllegalArgumentException("empty csv");

    String[] headers =
        lines
            .get(0)
            .split(
                ","); // grabs the first line (0) index, and splits the line into every part where
    // the comma will split
    Map<String, Integer> idx = new HashMap<>();
    for (int i = 0; i < headers.length; i++)
      idx.put(
          headers[i].trim().toLowerCase(),
          i); // removes spaces and will make everything lower case, inserting into a hashmap

    // following hashmap operators will examine for the label name to see the header name
    Integer iName = idx.getOrDefault("name", idx.getOrDefault("airport_name", 0));
    Integer iLat = idx.getOrDefault("latitude", idx.getOrDefault("lat", 1));
    Integer iLon = idx.getOrDefault("longitude", idx.getOrDefault("lon", 2));

    List<City> list = new ArrayList<>();
    for (int r = 1; r < lines.size(); r++) {
      String row =
          lines
              .get(r)
              .trim(); // again we are trimming each line of our csv, for this instance we will look
      // at one line
      if (row.isEmpty()) continue; // skips empty lines (not needed for our csv but just in case)
      String[] t = row.split(","); // split lines at commas and put them into this new list t
      try {
        String name =
            t[iName].trim(); // will find the proper indexing using iX and will trim all extra
        // spaces off of it
        double lat = Double.parseDouble(t[iLat].trim());
        double lon = Double.parseDouble(t[iLon].trim());
        list.add(
            new City(
                name, lat,
                lon)); // put all the trimmed info from the csv into the new ArrayList and will
        // return it
      } catch (Exception ignore) {

      }
    }
    return list;
  }

  /**
   * Main Method for tsp
   *
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    InputStream csvStream = //we will now input the csv
        HillClimbing.class.getClassLoader().getResourceAsStream("international_airports.csv");
 
    if (csvStream == null) {
      throw new FileNotFoundException("international_airports.csv NOT found in resources folder.");
    }

    List<City> cities = loadAirports(csvStream);

    int n = cities.size();
    System.out.println("Loaded " + n + " airports.");

    double[] xs = new double[n]; //lit of x values for each airport
    double[] ys = new double[n]; //list of y values for each arport
    for (int i = 0; i < n; i++) {
      xs[i] = cities.get(i).lon; //x = lon
      ys[i] = cities.get(i).lat; //y = lat
    }

    TSP.DoubleMatrix tsp = new TSP.DoubleMatrix(xs, ys);

    Configurator.configureRandomGenerator(213);

    PermutationInitializer init = new PermutationInitializer(n);

    final int restarts = 10;
    FirstDescentHillClimber<Permutation> hcFirstReversal =
        new FirstDescentHillClimber<>(tsp, new ReversalMutation(), init);
    SolutionCostPair<Permutation> solutionHCFirstReversal = hcFirstReversal.optimize(restarts);

    FirstDescentHillClimber<Permutation> hcFirstInsertion =
        new FirstDescentHillClimber<>(tsp, new InsertionMutation(), init);
    SolutionCostPair<Permutation> solutionHCFirstInsertion = hcFirstInsertion.optimize(restarts);

    FirstDescentHillClimber<Permutation> hcFirstSwap =
        new FirstDescentHillClimber<>(tsp, new SwapMutation(), init);
    SolutionCostPair<Permutation> solutionHCFirstSwap = hcFirstSwap.optimize(restarts);

    SteepestDescentHillClimber<Permutation> hcSteepReversal =
        new SteepestDescentHillClimber<>(tsp, new ReversalMutation(), init);
    SolutionCostPair<Permutation> solutionHCSteepReversal = hcSteepReversal.optimize(restarts);

    SteepestDescentHillClimber<Permutation> hcSteepInsertion =
        new SteepestDescentHillClimber<>(tsp, new InsertionMutation(), init);
    SolutionCostPair<Permutation> solutionHCSteepInsertion = hcSteepInsertion.optimize(restarts);

    SteepestDescentHillClimber<Permutation> hcSteepSwap =
        new SteepestDescentHillClimber<>(tsp, new SwapMutation(), init);
    SolutionCostPair<Permutation> solutionHCSteepSwap = hcSteepSwap.optimize(restarts);

    // choose the best of all runs
    List<SolutionCostPair<Permutation>> all =
        Arrays.asList(
            solutionHCFirstReversal,
            solutionHCFirstInsertion,
            solutionHCFirstSwap,
            solutionHCSteepReversal,
            solutionHCSteepInsertion,
            solutionHCSteepSwap);

    SolutionCostPair<Permutation> best = all.get(0);
    // equivalent using an index
    for (int i = 0; i < all.size(); i++) {
      SolutionCostPair<Permutation> s = all.get(i);
      if (s.getCost() < best.getCost()) best = s;
    }

    System.out.print(best.getCost()); //"%nBest tour cost: %.3f%n", 
    System.out.println(
        "Tour order (indices in CSV): " + Arrays.toString(best.getSolution().toArray()));

    System.out.println("\nTour names:");
    for (int i = 0; i < n; i++) {
      int idx = best.getSolution().get(i);
      System.out.println((i + 1) + ". " + cities.get(idx).name);
    }
  }
}
