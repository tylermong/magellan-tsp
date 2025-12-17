# Magellan-TSP

## Description

Magellan-TSP is a Java-based simulation of the Traveling Salesperson Problem (TSP) that aims to visit one international airport in each country using real-world distances. The project implements multiple algorithms to determine near-optimal routes based on these distances, and the results are documented and analyzed in an accompanying report.

## Features

[TODO]

## Installation

[TODO]

## Usage

[TODO]

## Project Structure

[TODO]

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).

## How to run program
java -jar ./target/magellan-tsp-1.0-SNAPSHOT.jar

## Team Workload Allocation

The general methodology of assigning work to group members was to assign roles to members according to their strengths.
For example, Tyler and Alexis were assigned to be the main code reviewers as they were the most experienced in programming. Tyler also headed the role of providing the rest of the group with seeded runs so that we had more direction and impact with our algorithms. Alexis came up with the idea for the project and acted as a project lead as well.
Tasnim was assigned the role of implementing Haversine Distance given her strength in math. Michael and Owen worked on the bulk of the documentation including this, the report, and the project proposal. 

In terms of coding contributions every group member was given at the least one algorithm to implement. These are the coding some of the coding contributions listed out.

Alexis : Order Crossover. Partially Matched Crossover, Cycle Crosssover. Also added a 3-opt local search to optimize and show how much of a difference it makes in finding a better run. In addition to that he created the airport csv and airport class.

Tasnim : Haversine Distance implementation that was used in each member's algorithm classes. Also did a generational evolutionary computation using permutation, elitism and Cycle Crossover, Partially Matched Crossover, and Cycle Crossover.

Owen : Steepest and first hill climbing, including swap insertion and reversal mutations for both of them.

Michael : Implemented Simulated Annealing, comparing insertion vs swap mutations across multiple randomized runs and reported best-tour rankings and runtime statistics.

Tyler : Created and managed the github for the entire project. Created the framework for the project and implemented RepeatedNearestNeighbor which generated a seed for use in everyone else's code to make the runs more impactful and connected.