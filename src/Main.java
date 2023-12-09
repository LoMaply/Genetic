import geneticsteps.Crossover;
import geneticsteps.Stochastic;
import geneticsteps.Gene;
import geneticsteps.Population;
import model.Person;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static int POPULATION_SIZE = 50; // No. of genes in each population.
    public static int GENE_LENGTH = 20; // No. of Person objects in each gene.
    public static int GROUP_NUMBER = 5; // No. of equal sized groups to split Person objects into.
    public static int GENERATION_COUNT = 500; // Max no. of generations to run.
    public static double FITNESS_LIMIT = 6.2; // Minimum fitness for stopping algo.
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

    /**
     * Creates a custom gene, allows testing with constant data as opposed to random data.
     * @return Custom user defined gene.
     */
    public static Person[] createCustomGene() {
        Person[] custom = new Person[GENE_LENGTH];
        custom[0] = new Person(new int[]{}, new double[]{ 0.23, 0.50, 0.70 }, new double[]{ 0.5, 0.6, 0.7 });
        custom[1] = new Person(new int[]{}, new double[]{ 0.8, 0.6, 0.45 }, new double[]{ 0.32, 0.59, 0.67 });
        custom[2] = new Person(new int[]{ 0 }, new double[]{ 0.6, 0.6, 0.7 }, new double[]{ 0.4, 0.45, 0.80 });
        custom[3] = new Person(new int[]{ 4 }, new double[]{ 0.7, 0.73, 0.78 }, new double[]{ 0.67, 0.65, 0.60 });
        custom[4] = new Person(new int[]{ 3 }, new double[]{ 0.55, 0.83, 0.70 }, new double[]{ 0.89, 0.59, 0.55 });
        custom[5] = new Person(new int[]{}, new double[]{ 0.80, 0.67, 0.45 }, new  double[]{ 0.90, 0.75, 0.87 });
        custom[6] = new Person(new int[]{ 14 }, new double[]{ 0.63, 0.50, 0.40 }, new double[]{ 0.67, 0.75, 0.45 });
        custom[7] = new Person(new int[]{ 1 }, new double[]{ 0.72, 0.61, 0.75 }, new double[]{ 0.52, 0.59, 0.77 });
        custom[8] = new Person(new int[]{}, new double[]{ 0.65, 0.58, 0.78 }, new double[]{ 0.39, 0.85, 0.90 });
        custom[9] = new Person(new int[]{}, new double[]{ 0.72, 0.63, 0.82 }, new double[]{ 0.66, 0.65, 0.14 });
        custom[10] = new Person(new int[]{ 18 }, new double[]{ 0.25, 0.40, 0.60 }, new double[]{ 0.90, 0.50, 0.45 });
        custom[11] = new Person(new int[]{}, new double[]{ 0.80, 0.77, 0.43 }, new  double[]{ 0.79, 0.65, 0.57 });
        custom[12] = new Person(new int[]{ 10 }, new double[]{ 0.72, 0.73, 0.67 }, new double[]{ 0.57, 0.65, 0.62 });
        custom[13] = new Person(new int[]{}, new double[]{ 0.55, 0.73, 0.90 }, new double[]{ 0.89, 0.69, 0.45 });
        custom[14] = new Person(new int[]{ 6 }, new double[]{ 0.81, 0.68, 0.43 }, new  double[]{ 0.53, 0.29, 0.37 });
        custom[15] = new Person(new int[]{ 3, 8 }, new double[]{ 0.62, 0.56, 0.43 }, new double[]{ 0.67, 0.65, 0.55 });
        custom[16] = new Person(new int[]{}, new double[]{ 0.62, 0.68, 0.71 }, new double[]{ 0.52, 0.64, 0.75 });
        custom[17] = new Person(new int[]{}, new double[]{ 0.65, 0.48, 0.68 }, new double[]{ 0.38, 0.82, 0.19 });
        custom[18] = new Person(new int[]{ 12 }, new double[]{ 0.74, 0.63, 0.72 }, new double[]{ 0.46, 0.65, 0.14 });
        custom[19] = new Person(new int[]{}, new double[]{ 0.26, 0.58, 0.85 }, new double[]{ 0.84, 0.53, 0.49 });

        return custom;
    }


    public static void main(String[] args) {
        // RNG for various random no. selections
        Random random = new Random();

        // Create population, geneLength and size must be > 1 at all times, I won't bother with error checking.
        // Replace createCustomGene() argument with null to test with random data (no preferences) instead of same data.
        Population population = Population.initialise(GENE_LENGTH, POPULATION_SIZE, GROUP_NUMBER, createCustomGene(), new int[]{ 1, 11 }, new int[]{ 0, 16 });
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

            // Elitism (Create new population using children + fittest parents)
            ArrayList<Gene> childrenPopulation = population.getFittestGenes(POPULATION_SIZE - OFFSPRING_COUNT);
            childrenPopulation.addAll(selectedGenes);
            population.updateGenes(childrenPopulation);

            // Printing outputs after each generation
            System.out.println("Generation: " + count);
            population.printTotalFitness();

            count++;
        }

        population.printPopulation();
        Gene fittest = population.getFittestGenes(1).get(0);
        System.out.println("\n\nFittest gene: " + fittest.toString());
        fittest.printAsGroup();
    }
}