#### Introduction
Do you, a pilot, perhaps see yourself flying to every country in constant succession someday? Hopping from airport to airport comes at some physical cost, as fuel and time are of the essence. Well, we propose an idea to help you figure out how to make such a trek without using all your fuel. We plan to solve the Travelling Salesperson Problem (TSP) using “Evolutionary Computation” relevant algorithms to optimize your path to be as short as possible!

Important to note is how we had initially thought out our plan to do a Magellan-like-TSP. The properties follow that each run of our TSP would require each nation having one airport that we will reach, and the lightest-distance path shall be returned. The properties of our Magellan-style TSP involve a CSV containing each airport's name, the country each airport resides in, and most importantly, their longitude and latitude. The main algorithms we had decided to run this TSP on were steepest and first hill climbers, simulated annealing, crossovers, and others. 

The goal of this project was to implement and do a deeper dive into evolutionary computation methods, a subsector of artificial intelligence algorithms. These algorithms are inspired by real biological properties like evolution and mutation. Social Darwinistic methods of altering data were also thought of to solve optimization problems, sometimes we may find it best to keep the most elite members of a population, whilst manipulating the rest. We find these algorithms to be useful to our problem as the optimal-TSP is under the NP Hard category, so these algorithms will have self-employed ways to escape local optima.

To accomplish this project in time our group had sought out messaging platform “Discord” for communication, and had decided to split the algorithms of choosing equally so each member can hone on a specific sub-section of evolutionary computation. Repository owner tylermong had set up the repository to allow our workflow to happen mainly in GitHub, and provided a guide on the steps to creating an issue, creating a pull request, etc. This production flow allowed everyone to manage their own classes locally and understand more of the project as a whole.

#### Methods
To gather our airport data we used ourairports.com and their free-to-use CSV where we choose which airport from which country we will keep. We had used OpenAI’s ChatGPT to condense our list down to one airport per country, and furthermore, we had cleaned up any ailments left in its tracks. Additional clean-ups involved us replacing Generative AI-chosen airports for more relevant airports. 

The progression of our project was coordinated for each team member to get a specific subsection of evolutionary computation and apply it to our problem. In doing so, each of our team members were given an opportunity to refine their understanding on these specific algorithms, as well as learn how to apply Dr. Vincent Cicirello’s “Chips-n-Salsa” library to computational problems, as well as approach the algorithms from the ground up. Importantly, to keep our code organized and uniform will be the implementation of the Spotify Formatter Plugin.

One of the first thoughts circulated regarding this project was implementing a Haversine distance class which would be implemented in every member's methods. The Haversine Formula is a way of calculating the shortest distance between two spots on a sphere. 

Discussion over what algorithms and or methods we would use started with an emphasis on crossovers to solve our TSP, such as order crossover (OX), partially matched crossover (PMX), and cycle crossover (CX). Each one of these methods also got modified to include a 3-opt local search mutation which was to show the difference it made in pulling better results. 3-opt made sure we kept good candidates because tournament selection had too much randomness in what it kept and discarded.Generational Evolutionary Computation, incorporating a strict elitism mechanic akin to "Social Darwinism" where only the fittest individuals survive to influence the next generation. To drive evolution and maintain structural validity in the permutations, the algorithm utilizes specific recombination operators: Partially Mapped Crossover (PMX) and Cycle Crossover (CX). These methods preserve critical genetic information from parents while exploring the solution space, iteratively refining the population toward an optimal outcome. Even the basic hill climbers were considered due to the mutations that can be added onto them, such as insertion, reversal, and swap. Another spin on a simpler take was applying swap mutations and insertion mutations to simulated annealing, and seeing how they stack up against each other over multiple randomized runs. 

The overall progression for each of the previously mentioned methods and mutations involves using the TSP method from the Chips-n-Salsa Library to determine Euclidean distances, and then using our Haversine class we will convert those to Haversine distances. Doing this we will be able to compare the results given from our implemented methods.


#### Results
With regard to crossover operators, we quickly found that while they adequately randomized the population to create diverse generations, we lacked an effective method for preserving high-quality solutions. Tournament selection’s inherent randomness appeared to cause the loss of several promising parents that may have contributed to better future generations, and our results reflected this issue. Even Order Crossover, our strongest performer among the three tested (Order, Partially Matched, and Cycle), produced outcomes that fell short when compared to other genetic algorithm approaches we explored.

 ![Crossover TSP Output](main/outputs/CrossoverTSP-output.png)
 
It was only after implementing a 3-Opt local search operator that we observed noticeably improved and more consistent results. Even then, our findings suggest that although crossover effectively generates variation and helps maintain population diversity, it is insufficient on its own when attempting to optimize and reach high-quality solutions for this problem.

Tasnim's implementation of Genetic Evolutionary Computation experiments pointed to Order Crossover (OX) as the clear winner, securing the best route with a total distance of about 1.06 million km. Partially Mapped Crossover (PMX) performed overall well,  while Cycle Crossover (CX) had a harder time, falling 18% behind the top score. We saw consistent convergence across all 20 runs, and it became obvious that mutation was the key factor helping the system avoid getting stuck in local loops (local optima) to find a better final path.

 ![Generational Evolutionary Computation Output](magellan-tsp/src/main/outputs/gen-evo-output.jpg)

Michael started by testing an insertion-style mutation on its own to see how well it could solve the TSP:

 ![Insertion Mutation Output](magellan-tsp/src/main/outputs/InsertionMutationResults.png)
 
The mutation was able to make reasonable local changes to the tour, but it often got stuck in local minima and stopped improving after a while. Results also varied a lot between runs, which suggested that mutation alone was not reliable for consistently finding good solutions.

We then applied simulated annealing using a two-change mutation operator:

 ![Simulated Annealing Output](magellan-tsp/src/main/outputs/SimulatedAnnealingResults.png)

This approach produced better and more consistent tour costs across multiple runs. By occasionally accepting worse solutions early on, simulated annealing was able to escape local minima and continue improving the tour, which made it clearly more effective than using insertion-style mutation by itself.

For our greedy hill climbers, implementation using Vincent Cicirello's "Chips-n-Salsa" was chosen to apply both first descent and steepest descent hill climbing. Since this is a reduction problem, trying to get the shortest distance, we will be doing descent compared to ascent. Within these two we will apply 3 different mutation operators to them: swap, insertion, and reversal: 

 ![Hill Climbing TSP Output](magellan-tsp/src/main/outputs/HillClimbingTSP-output.png)

Within this output, we can see that while the reversal mutation yielded the lowest tour, the steepest descent approach had proven slightly better than first descent. This output also shows that the swap mutation might not be viable for hill climbing, as the outputs we got are way too large in comparison to that of reversal and insertion mutation application.
