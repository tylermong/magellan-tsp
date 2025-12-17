# Magellan-TSP

## Description

Magellan-TSP is a Java-based simulation of the Traveling Salesperson Problem (TSP) that aims to visit one international airport in each country using real-world distances. The project implements multiple algorithms to determine near-optimal routes based on these distances, and the results are documented and analyzed in an accompanying report.

## Features

-   **Real-world Data**: Uses a dataset of international airports (`international_airports.csv`) with latitude and longitude.
-   **Haversine Distance**: Calculates accurate distances between coordinates on the globe.
-   **Multiple Algorithms**:
    -   **Hill Climbing**: Steepest Descent and First Descent with various mutations (Swap, Insertion, Reversal).
    -   **Simulated Annealing**: Optimization with temperature decay to escape local optima.
    -   **Evolutionary/Genetic Algorithms**: Crossover operators (Order, Partially Matched, Cycle) combined with 3-opt local search.
    -   **Generational Evolutionary Computation**: Uses permutation, elitism, and various crossover strategies (Order, PMX, Cycle).
    -   **Repeated Nearest Neighbor**: Baseline algorithm for comparison.
-   **Performance Analysis**: Compares the tour lengths and execution times of different approaches.

## Installation

### Prerequisites
-   Java Development Kit (JDK) 21 or higher.
-   Apache Maven.

### Build and Run
1.  Clone the repository:
    ```bash
    git clone https://github.com/tylermong/magellan-tsp
    cd magellan-tsp
    ```

2.  Build the project using Maven:
    ```bash
    mvn clean package
    ```
    This will compile the code and generate an executable JAR file in the `target` directory.

3.  Run the project:
    ```bash
    java -jar target/magellan-tsp-1.0-SNAPSHOT.jar
    ```

## Usage

The application will load the airport data, build a distance matrix, and sequentially run the following experiments, outputting the results to the console:
1.  Local Search (Hill Climbing)
2.  Simulated Annealing
3.  Insertion Mutation
4.  Crossover (Genetic Algorithms)
5.  Repeated Nearest Neighbor

## Project Structure

The source code is organized into the following packages under `edu.stockton.csci4510.team1.magellantsp`:

-   **`magellantsp`**: Contains the `Main` class which serves as the entry point.
-   **`crossover`**: Implementation of genetic algorithm crossover operators (Order, PMX, Cycle).
-   **`greedy`**: Repeated Nearest Neighbor implementation.
-   **`hillclimbing`**: Hill climbing algorithms (Steepest/First Descent).
-   **`simulatedannealing`**: Simulated annealing and specific mutation strategies.
-   **`util`**: Utility classes including `Airport` data structure, `AirportLoader` for parsing CSVs, and `HaversineDistance` for calculation.

## Team Workload Allocation

The general methodology of assigning work to group members was to assign roles to members according to their strengths.
For example, Tyler and Alexis were assigned to be the main code reviewers as they were the most experienced in programming. Alexis came up with the idea for the project and acted as a project lead as well.
Tasnim was assigned the role of implementing Haversine Distance given her strength in math. Michael and Owen worked on the bulk of the documentation including this, the report, and the project proposal. 

In terms of coding contributions every group member was given at the least one algorithm to implement. These are the coding contributions listed out:

-   **Alexis**: Order Crossover, Partially Matched Crossover, Cycle Crossover. Also added a 3-opt local search to optimize and show how much of a difference it makes in finding a better run. In addition to that he created the airport csv and airport class.
-   **Tasnim**: Haversine Distance implementation that was used in each member's algorithm classes. Also did a generational evolutionary computation using permutation, elitism and Order Crossover, Partially Matched Crossover, and Cycle Crossover.
-   **Owen**: Steepest and first hill climbing, including swap insertion and reversal mutations for both of them.
-   **Michael**: Implemented Simulated Annealing, comparing insertion vs swap mutations across multiple randomized runs and reported best-tour rankings and runtime statistics.
-   **Tyler**: Created and managed the github for the entire project. Created the framework for the project and implemented RepeatedNearestNeighbor.

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).
