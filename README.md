# Genetic Algorithm for Group Matching

This is a repository to test a prototype implementation of a genetic algorithm for matching individuals into groups based on several characteristics.
The actual algorithm to be used/tested on student groups in NUS will be [implemented here](https://github.com/IEX-Team-Laddy).

This algorithm is based on the [following paper](https://www.sciencedirect.com/science/article/abs/pii/S0950705117304914#sec0013) by Yaqian Zheng, Chunrong Li, Shiyu Liu and Weigang Lu.

The following are some settings for the algorithm as detailed in the paper under Section 5.3

- Generation Gap = 0.9
- Crossover Operator Probability = 0.9
- Mutation Operator Probability = 0.09


# Usage

### Modifying settings

Simply run `Main`. Several settings to take note of below:

There are 2 main classes where the user can change the algorithm settings by modifying the values of the class variables as follows:
1. Weight.java - Allows user to modify the weights of various groups, which impacts the calculated Fitness of a particular Gene.
2. Main.java - Allows user to modify the various probability chances, generation count, population count, gene size etc.

### Fitness Limit

When the algorithm is first run on a new set of data/settings, first set the `FITNESS_LIMIT` in Main.java to a high value. This is due to the behaviour of the algorithm where throughout each generation, Population Fitness increases to a maximum before decreasing slightly and staying at a roughly even level.

As such, `FITNESS_LIMIT` is here to simply help capture the state of the Population at this peak Fitness based on observation of initial results.
The reference paper seems to allude to this behaviour briefly on pg 220 where during testing with other algorithms, both `POPULATION_SIZE` and `GENERATION_COUNT` had to be fined tuned.

### Custom Gene

Go to line 69. This is where the Population is initialised using the various user defined settings.

Arguments 1, 2 and 3 should be modified from the class variables at the top of the Main class.

The 4th argument can take in 2 possible values:
1. `null`, which causes the algorithm to be run on a Gene of randomly generated Person objects. These randomly generated objects will not have any preferences.
2. `createCustomGene()`, which causes the algorithm to run on a Gene of user created Person objects. This allows for testing on the same data. Ensure that every index of the custom gene contains a Person, and that the length of the custom gene == `GENE_LENGTH`.

### Aggregation/Distribution

The 5th and 6th argument are int arrays taking in a variable list of Person IDs and represent Person objects that ought to be grouped together, or separated, respectively.
1. For instance, putting `1` and `11` in the 5th argument means that Genes where `Person 1` and `Person 11` are in the same group will be considered fitter (depending on the value of `WEIGHT_DISTRIBUTION` in the Weight class)
2. On the other hand, putting `0` and `16` in the 6th argument means that Genes where `Person 0` and `Person 16` are in different groups will be considered fitter (also depending on the value of `WEIGHT_DISTRIBUTION` in the Weight class)
3. Ensure that the integers input into these arrays are never >= `GENE_LENGTH`. (Person IDs are zero-indexed by default).
4. For any 2 Person IDs, they should not simultaneously be in both int arrays at the same time. (Logically speaking, 2 individuals cannot be grouped together AND separated at the same time)


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
2. Invert mutation on the other hand inverts a subsection of the gene. However, the result of this is accepted ONLY IF the result is fitter than the input.

The paper does not specify whether Swap mutation has the same `result fitter than input` condition as the Invert mutation, for now I will assume that it does not. This has the side effect where after sufficient iterations of the algorithm, Fitness seems to decrease and stabilize after reaching its peak fitness. However, this stabilized fitness is often still more than the initial population fitness.

The temporary solution for this is to introduce a `FITNESS_LIMIT` variable that stops the algorithm when a certain fitness threshold is met, as only the data of the final Population is written to the console.

Paper also doesn't specify whether Invert mutation occurs on a probability, but this is assumed to be yes.

## Issue 3

Not so much an issue with the paper, but a note on the current implementation of `fPref` (preference of being grouped with a particular Person) when calculating fitness.

The paper uses a 1 - 5 normalized scale to calculate `fPref`.

In the current implementation, a `Person A` has an integer array containing the IDs of other people `Person A` prefers to be grouped with. All these people will be assigned the value `1`, while all other people are assigned `0` when it comes to calculating `fPref` with respect to `Person A`.
