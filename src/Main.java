import geneticsteps.Crossover;
import geneticsteps.Stochastic;
import model.Gene;
import model.Population;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static int POPULATION_SIZE = 50; // No. of genes in each population.
    public static int GENE_LENGTH = 20; // Length of each gene, populates Gene with numbers from 1 to n.
    public static int GENERATION_COUNT = 10000; // Max no. of generations.
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
        Population population = new Population(GENE_LENGTH, POPULATION_SIZE);
        population.printPopulation();
        System.out.println("\n");

        int count = 0;
        while(count < GENERATION_COUNT) {

            // Stochastic Universal Sampling
            // No. of selected parents = OFFSPRING_COUNT, Every parent pair produces 2 children.
            List<Gene> selectedGenes = Stochastic.selectGenes(population, OFFSPRING_COUNT);

            // Used as upper bound in random variable to select 2 random points in a gene for Crossover + Mutation Operation
            int size = GENE_LENGTH - 1;

            // Crossover Operation
            for (int i = 0; i < OFFSPRING_COUNT; i += 2) {
                if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                    Pair<Gene, Gene> children = Crossover.pmxCrossover(selectedGenes.get(i), selectedGenes.get(i + 1), random.nextInt(size), random.nextInt(size));
                    selectedGenes.set(i, children.getKey());
                    selectedGenes.set(i + 1, children.getValue());
                }
            }

            // Mutation 1 and 2
            for (int i = 0; i < OFFSPRING_COUNT; i++) {
                // Swap Mutation
                if (random.nextDouble() <= MUTATION_PROBABILITY) {
                    selectedGenes.set(i, selectedGenes.get(i).mutateSwap(random.nextInt(size), random.nextInt(size)));
                }
                // Invert Mutation
                if (random.nextDouble() <= MUTATION_PROBABILITY) {
                    selectedGenes.set(i, selectedGenes.get(i).invert(random.nextInt(size), random.nextInt(size)));
                }
            }

            // Elitism
            ArrayList<Gene> childrenPopulation = population.getFittestGenes(POPULATION_SIZE - OFFSPRING_COUNT);
            childrenPopulation.addAll(selectedGenes);
            population.updateGenes(childrenPopulation);

            System.out.println("Generation: " + count);
            population.printTotalFitness();

            count++;
        }

        population.printPopulation();
    }
}