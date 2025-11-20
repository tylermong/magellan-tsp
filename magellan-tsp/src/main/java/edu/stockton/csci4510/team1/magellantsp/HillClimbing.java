package edu.stockton.csci4510.team1.magellantsp;

import java.io.*;
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
  static List<City> loadAirports(Path csv) throws IOException {
    List<String> lines = Files.readAllLines(csv); // gathers all the lines to process
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

    // following hashmap operators will examine for the lkabel name to see the header name
    Integer iName = idx.getOrDefault("name", idx.getOrDefault("airport_name", 0));
    Integer iLat = idx.getOrDefault("latitude", idx.getOrDefault("lat", 1));
    Integer iLon = idx.getOrDefault("longitude", idx.getOrDefault("lon", 2));

    List<City> list = new ArrayList<>();
    for (int r = 1; r < lines.size(); r++) {
      String line =
          lines
              .get(r)
              .trim(); // again we are trimming each line of our csv, for this instance we will look
      // at one line
      if (line.isEmpty()) continue; // skips empty lines (not needed for our csv but just in case)
      String[] t = line.split(","); // split lines at commas and put them into this new list t
      try {
        String name =
            t[iName].trim(); // will find the proper indexing using iX and and will trim all extra
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

  public static void main(String[] args) throws Exception {

    String fallback =
        System.getProperty("user.home")
            + "/IdeaProjects/ProjectTwo/magellan-tsp/magellan-tsp/src/main/resources/international_airports.csv";

    Path csv = Paths.get(args.length > 0 ? args[0] : fallback);

    if (!Files.exists(csv)) {
      throw new FileNotFoundException("CSV file not found: " + csv);
    }

    if (!Files.exists(csv)) {
      System.err.println("Airport CSV not found at: " + csv.toString());
      System.err.println("Pass path as first argument or place CSV at the default location.");
      System.exit(2);
    }

    List<City> cities = loadAirports(csv);
    int n = cities.size();
    System.out.println("Loaded " + n + " airports.");

    double[] xs = new double[n];
    double[] ys = new double[n];
    for (int i = 0; i < n; i++) {
      xs[i] = cities.get(i).lon;
      ys[i] = cities.get(i).lat;
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

    // for right now, just to test when i figure out to run
    System.out.println("------------------------------");
    System.out.println("HILL CLIMBERS");
    System.out.println("------------------------------");
    System.out.printf("%-20s%15s%n", "search_operator", "distance");
    System.out.println("------------------------------");
    System.out.printf("%-20s%15.3f%n", "first_reversal", solutionHCFirstReversal.getCost());
    System.out.printf("%-20s%15.3f%n", "first_insertion", solutionHCFirstInsertion.getCost());
    System.out.printf("%-20s%15.3f%n", "first_swap", solutionHCFirstSwap.getCost());
    System.out.printf("%-20s%15.3f%n", "steepest_reversal", solutionHCSteepReversal.getCost());
    System.out.printf("%-20s%15.3f%n", "steepest_insertion", solutionHCSteepInsertion.getCost());
    System.out.printf("%-20s%15.3f%n", "steepest_swap", solutionHCSteepSwap.getCost());
    System.out.println("------------------------------");

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
    for (SolutionCostPair<Permutation> s : all) {
      if (s.getCost() < best.getCost()) best = s;
    }

    System.out.printf("%nBest tour cost: %.3f%n", best.getCost());
    System.out.println(
        "Tour order (indices in CSV): " + Arrays.toString(best.getSolution().toArray()));

    System.out.println("\nTour names:");
    for (int i = 0; i < n; i++) {
      int idx = best.getSolution().get(i);
      System.out.println((i + 1) + ". " + cities.get(idx).name);
    }
  }
}
