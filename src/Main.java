import geneticsteps.Crossover;
import geneticsteps.Stochastic;
import geneticsteps.Gene;
import geneticsteps.Population;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static int POPULATION_SIZE = 50; // No. of genes in each population.
    public static int GENE_LENGTH = 30; // No. of Person objects in each gene.
    public static int GROUP_NUMBER = 6; // No. of equal sized groups to split Person objects into.
    public static int GENERATION_COUNT = 250; // Max no. of generations to run.
    public static int FITNESS_LIMIT = 5; // Minimum fitness for stopping algo.
    public static double GENERATION_GAP = 0.9; // Ratio of parent population to child population, must be 0 < x < 1. POPULATION_SIZE * GENERATION_GAP = no. of child genes needed. Remaining space is saved for fittest parents.
    public static int OFFSPRING_COUNT = getOffspringCount(); // No. of offspring, must be multiple of 2.
    public static double CROSSOVER_PROBABILITY = 0.9; // Chance of crossover operation.
    public static double MUTATION_PROBABILITY = 0.09; // Chance of applying either mutation operations.

    /**
     * Calculates no. of offspring to generate based on POPULATION_SIZE & GENERATION_GAP, that is a multiple of 2.
     */
    public static int getOffspringCount() {
        int count = (int) (POPULATION_SIZE * GENERATION_GAP);
        return (count % 2) == 0 ? count : count + 1;
    }


    public static void main(String[] args) {
        // RNG for various random no. selections
        Random random = new Random();

        // Create population, geneLength and size must be > 1 at all times, I won't bother with error checking.
        Population population = Population.initialise(GENE_LENGTH, POPULATION_SIZE, GROUP_NUMBER);
        population.printPopulation();
        System.out.println("\n");

        int count = 0;
        while (count < GENERATION_COUNT && population.getTotalFitness() < FITNESS_LIMIT) {

            // Stochastic Universal Sampling
            // No. of selected parents = OFFSPRING_COUNT, Every parent pair produces 2 children.
            List<Gene> selectedGenes = Stochastic.selectGenes(population, OFFSPRING_COUNT);

            // Used as upper bound when generating random integer to select 2 random points in a gene for Crossover and Mutation Operation
            int limit = GENE_LENGTH - 1;

            // Crossover Operation
            for (int i = 0; i < OFFSPRING_COUNT; i += 2) {
                if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                    Pair<Gene, Gene> children = Crossover.pmxCrossover(selectedGenes.get(i), selectedGenes.get(i + 1), random.nextInt(limit), random.nextInt(limit));
                    selectedGenes.set(i, children.getKey());
                    selectedGenes.set(i + 1, children.getValue());
                }
            }

            // Mutation 1 and 2
            for (int i = 0; i < OFFSPRING_COUNT; i++) {
                // Swap Mutation
                if (random.nextDouble() <= MUTATION_PROBABILITY) {
                    selectedGenes.set(i, selectedGenes.get(i).mutateSwap(random.nextInt(limit), random.nextInt(limit)));
                }
                // Invert Mutation
                if (random.nextDouble() <= MUTATION_PROBABILITY) {
                    selectedGenes.set(i, selectedGenes.get(i).mutateInvert(random.nextInt(limit), random.nextInt(limit)));
                }
            }

            // Elitism (Move parents with highest fitness to new population)
            ArrayList<Gene> childrenPopulation = population.getFittestGenes(POPULATION_SIZE - OFFSPRING_COUNT);
            childrenPopulation.addAll(selectedGenes);
            population.updateGenes(childrenPopulation);

            // Printing outputs after each generation
            System.out.println("Generation: " + count);
            population.printTotalFitness();

            count++;
        }

        population.printPopulation();
    }
}