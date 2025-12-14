#### Results
With regard to crossover operators, we quickly found that while they adequately randomized the population to create diverse generations, we lacked an effective method for preserving high-quality solutions. Tournament selection’s inherent randomness appeared to cause the loss of several promising parents that may have contributed to better future generations, and our results reflected this issue. Even Order Crossover, our strongest performer among the three tested (Order, Partially Matched, and Cycle), produced outcomes that fell short when compared to other genetic algorithm approaches we explored.

It was only after implementing a 3-Opt local search operator that we observed noticeably improved and more consistent results. Even then, our findings suggest that although crossover effectively generates variation and helps maintain population diversity, it is insufficient on its own when attempting to optimize and reach high-quality solutions for this problem.


