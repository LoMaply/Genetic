# Genetic Algo test

This is a repository to test an implementation of a genetic algorithm for team matching.

For now, the actual fitness function containing variables to match people is still being tested. For now this algorithm will judge fitness based on how close an array is to being sorted in ascending order.
- Fitness = Total sum of array indexes where i = arr\[i\]

The following are some of the settings for the algorithm as detailed by the [reference paper](https://www.sciencedirect.com/science/article/abs/pii/S0950705117304914#sec0013) (also uploaded to Teams)


- Generation Gap = 0.9
- Crossover Operator Probability = 0.9
- Mutation Operator Probability = 0.09



# Developer notes

I took some liberties on certain sections of the algorithm not specified by the paper

## Issue 1

Based on the paper

1. Subset of population is chosen by SUS. This subset (known as the parent group) will be used in the crossover operation.
2. Every pair of parents in the parent group undergoes the Crossover operation (based on Crossover Probability). If 2 parents happen to not undergo crossover, the children will be exactly the same as parents.

Potential issue: Since there is a chance that the children are the same as the parents, and that these parents are already the fittest, doesn't it mean that there is a very small chance for multiple copies of the same parent to end up in the data?
Not sure if this is okay or not based on the description of the algo implementation in the paper.

For now I'm going to accept duplicate genes and see how it turns out.

### Chatgpt ans:

Whether or not you should prevent duplicates in your genetic algorithm depends on the specifics of your optimization problem and the characteristics of the solutions space. Here are some considerations:

**Advantages of Allowing Duplicates:**
1. **Preservation of Good Solutions:** Allowing duplicates through the Elitist Strategy can help in preserving the fittest solutions from the previous generation, ensuring that highly fit individuals have a chance to be included in the next generation.

2. **Exploitation:** Duplicate individuals may reinforce the search around promising regions of the solution space, aiding in convergence towards optimal or near-optimal solutions.

**Considerations for Preventing Duplicates:**
1. **Diversity:** Too many duplicates might reduce the diversity of the population, potentially leading to premature convergence to a local optimum. This is especially relevant if your genetic algorithm needs to explore a large and complex solution space.

2. **Computational Efficiency:** If computational resources are a concern, preventing duplicates might be beneficial, as it avoids redundant evaluations of the same solutions.

**Recommendations:**
1. **Balance is Key:** Striking a balance between exploration and exploitation is crucial. Allowing some duplicates while maintaining diversity through other mechanisms (e.g., mutation) can be a good compromise.

2. **Experimentation:** Experiment with different settings, including variations in the crossover probability, mutation rate, and the Elitist Strategy, to see how they affect the algorithm's performance on your specific problem.

3. **Problem-Specific Considerations:** The impact of duplicates may vary based on the nature of your optimization problem. Some problems may benefit from a more diverse population, while others may require a more focused exploitation of good solutions.

In summary, there is no one-size-fits-all answer. It's recommended to experiment and analyze the behavior of your genetic algorithm with and without duplicates to determine the most effective approach for your particular problem.


## Issue 2

Based on the paper

1. Swap mutation is carried out by a probability, which swaps the position of 2 randomly selected indexes in a gene.
2. Invert mutation on the other hand inverts a subsection of the gene. However, the result of this is accepted ONLY IF the result is fitter than the input

The paper does not specify whether Swap mutation has the same `result fitter than input` condition as the Invert mutation, for now I will assume that it does not.

Paper also doesn't specify whether Invert mutation occurs on a probability, but this is assumed to be yes.

