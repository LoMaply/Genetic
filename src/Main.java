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
    public static int GENE_LENGTH = 35; // No. of Person objects in each gene.
    public static int GROUP_NUMBER = 7; // No. of equal sized groups to split Person objects into.
    public static int GENERATION_COUNT = 1000; // Max no. of generations to run.
    public static double FITNESS_LIMIT = 1.692; // Minimum fitness for stopping algo.
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
     * Ensure that every array index contains a Person object.
     * @return Custom user defined gene.
     */
    public static Person[] createCustomGene() {
        Person[] custom = new Person[GENE_LENGTH];
        custom[0] = new Person(new int[]{}, new double[]{ 0, 0, 0.25, 0.75, 1.00, 1.00, 1.00, 1.00, 1.00 }, new double[]{ 0.00, 0.00, 0, 0, 1, 0.0, 0.00 });
        custom[1] = new Person(new int[]{}, new double[]{ 0, 0, 0.25, 0.75, 1.00, 1.00, 1.00, 0.75, 1.00 }, new double[]{ 0.33, 1.00, 1, 1, 1, 0.0, 0.50 });
        custom[2] = new Person(new int[]{}, new double[]{ 0, 1, 0.75, 0.25, 0.50, 0.75, 0.25, 0.75, 0.75 }, new double[]{ 0.67, 1.00, 1, 1, 0, 0.5, 0.25 });
        custom[3] = new Person(new int[]{}, new double[]{ 1, 1, 0.50, 0.25, 1.00, 1.00, 0.00, 0.50, 0.75 }, new double[]{ 0.33, 0.33, 1, 0, 1, 0.5, 0.25 });
        custom[4] = new Person(new int[]{}, new double[]{ 0, 0, 0.75, 0.50, 0.50, 0.75, 0.75, 0.75, 0.50 }, new double[]{ 0.67, 1.00, 0, 1, 1, 0.5, 0.25 });

        custom[5] = new Person(new int[]{}, new double[]{ 0, 0, 0.50, 0.25, 0.75, 0.75, 0.75, 0.75, 0.50 }, new double[]{ 1.00, 0.33, 1, 1, 0, 0.0, 0.25 });
        custom[6] = new Person(new int[]{}, new double[]{ 0, 0, 0.25, 0.75, 0.50, 1.00, 0.00, 0.75, 1.00 }, new double[]{ 1.00, 1.00, 1, 1, 1, 0.5, 0.00 });
        custom[7] = new Person(new int[]{}, new double[]{ 0, 0, 0.00, 0.25, 0.75, 0.75, 0.00, 0.75, 0.75 }, new double[]{ 0.00, 0.00, 1, 1, 0, 1.0, 0.00 });
        custom[8] = new Person(new int[]{}, new double[]{ 0, 1, 0.00, 1.00, 1.00, 1.00, 1.00, 0.75, 0.75 }, new double[]{ 1.00, 1.00, 1, 1, 1, 0.5, 0.00 });
        custom[9] = new Person(new int[]{}, new double[]{ 0, 0, 0.75, 0.75, 1.00, 0.50, 1.00, 1.00, 0.25 }, new double[]{ 0.33, 0.33, 1, 0, 1, 0.5, 0.00 });

        custom[10] = new Person(new int[]{}, new double[]{ 0, 0, 0.50, 0.25, 1.00, 0.75, 1.00, 0.75, 0.75 }, new double[]{ 0.00, 0.00, 0, 0, 1, 0.5, 0.00 });
        custom[11] = new Person(new int[]{}, new double[]{ 0, 1, 0.50, 1.00, 1.00, 1.00, 0.75, 1.00, 0.75 }, new double[]{ 0.33, 0.33, 1, 1, 1, 0.0, 0.50 });
        custom[12] = new Person(new int[]{}, new double[]{ 1, 1, 0.25, 0.50, 0.75, 0.75, 0.50, 0.75, 0.75 }, new double[]{ 1.00, 0.33, 1, 0, 1, 0.5, 0.25 });
        custom[13] = new Person(new int[]{}, new double[]{ 1, 0, 0.75, 0.50, 1.00, 1.00, 0.75, 0.75, 0.50 }, new double[]{ 0.67, 0.67, 1, 0, 0, 0.5, 0.25 });
        custom[14] = new Person(new int[]{}, new double[]{ 1, 0, 0.75, 0.75, 0.75, 0.75, 1.00, 0.75, 0.75 }, new double[]{ 1.00, 1.00, 0, 0, 1, 0.0, 0.50 });

        custom[15] = new Person(new int[]{}, new double[]{ 0, 0, 1.00, 0.50, 0.75, 0.75, 0.50, 0.50, 0.50 }, new double[]{ 0.00, 0.67, 0, 0, 0, 0.5, 0.00 });
        custom[16] = new Person(new int[]{}, new double[]{ 1, 1, 1.00, 1.00, 1.00, 0.75, 0.50, 0.75, 0.25 }, new double[]{ 0.00, 1.00, 1, 0, 0, 0.5, 0.50 });
        custom[17] = new Person(new int[]{}, new double[]{ 0, 1, 0.75, 0.75, 0.75, 0.75, 0.00, 1.00, 0.00 }, new double[]{ 0.33, 0.33, 0, 1, 0, 0.0, 0.00 });
        custom[18] = new Person(new int[]{}, new double[]{ 0, 0, 0.75, 0.75, 1.00, 0.75, 1.00, 1.00, 0.75 }, new double[]{ 0.00, 0.33, 0, 0, 1, 0.5, 0.50 });
        custom[19] = new Person(new int[]{}, new double[]{ 1, 1, 0.00, 0.75, 0.75, 0.75, 0.50, 0.50, 0.50 }, new double[]{ 0.33, 0.33, 1, 0, 1, 1.0, 0.00 });

        custom[20] = new Person(new int[]{}, new double[]{ 0, 0, 0.75, 0.75, 0.50, 0.75, 1.00, 0.50, 0.25 }, new double[]{ 1.00, 1.00, 0, 0, 0, 0.0, 0.75 });
        custom[21] = new Person(new int[]{}, new double[]{ 1, 0, 0.25, 1.00, 0.25, 0.25, 1.00, 1.00, 0.75 }, new double[]{ 0.33, 0.33, 0, 0, 1, 0.0, 0.50 });
        custom[22] = new Person(new int[]{}, new double[]{ 1, 0, 0.50, 0.75, 0.50, 0.50, 0.75, 0.75, 0.75 }, new double[]{ 0.33, 0.33, 1, 0, 0, 0.0, 0.50 });
        custom[23] = new Person(new int[]{}, new double[]{ 0.5, 1, 0.75, 0.75, 0.75, 0.5, 0.0, 0.50, 0.75 }, new double[]{ 0.00, 0.00, 1, 0, 1, 0.5, 0.50 });
        custom[24] = new Person(new int[]{}, new double[]{ 0, 0, 0.75, 0.25, 0.75, 0.75, 0.25, 0.50, 0.25 }, new double[]{ 0.67, 1.00, 0, 0, 1, 0.0, 0.25 });

        custom[25] = new Person(new int[]{}, new double[]{ 0, 1, 0.75, 0.75, 0.50, 0.75, 1.00, 1.00, 0.75 }, new double[]{ 0.00, 1.00, 1, 1, 0, 0.0, 0.50 });
        custom[26] = new Person(new int[]{}, new double[]{ 0, 0, 1.00, 0.50, 0.25, 0.25, 0.75, 0.75, 0.50 }, new double[]{ 0.00, 0.33, 1, 0, 0, 0.0, 0.00 });
        custom[27] = new Person(new int[]{}, new double[]{ 1, 1, 0.25, 0.25, 0.50, 0.50, 0.75, 0.75, 0.75 }, new double[]{ 1.00, 0.33, 1, 1, 1, 0.5, 0.25 });
        custom[28] = new Person(new int[]{}, new double[]{ 1, 1, 0.75, 0.50, 0.75, 0.50, 1.00, 0.75, 0.50 }, new double[]{ 0.33, 0.67, 1, 1, 0, 0.5, 0.50 });
        custom[29] = new Person(new int[]{}, new double[]{ 0, 1, 0.25, 0.75, 0.75, 0.75, 0.75, 0.75, 0.50 }, new double[]{ 0.33, 0.33, 1, 1, 1, 0.5, 0.50 });

        custom[30] = new Person(new int[]{}, new double[]{ 1, 0, 0.50, 0.75, 0.75, 0.50, 0.25, 0.75, 0.25 }, new double[]{ 0.33, 0.33, 1, 1, 1, 0.0, 0.25 });
        custom[31] = new Person(new int[]{}, new double[]{ 1, 0, 0.75, 1.00, 0.75, 0.75, 0.75, 1.00, 0.75 }, new double[]{ 0.33, 1.00, 0, 0, 1, 0.5, 0.50 });
        custom[32] = new Person(new int[]{}, new double[]{ 1, 1, 0.50, 0.75, 1.00, 0.75, 0.75, 0.75, 0.75 }, new double[]{ 0.00, 0.00, 0, 1, 1, 0.5, 0.00 });
        custom[33] = new Person(new int[]{}, new double[]{ 1, 0, 1.00, 0.75, 0.75, 1.00, 0.75, 0.75, 1.00 }, new double[]{ 0.33, 0.33, 1, 1, 1, 0.0, 0.50 });
        custom[34] = new Person(new int[]{}, new double[]{ 1, 0, 0.75, 0.50, 0.75, 0.75, 0.75, 1.00, 0.50 }, new double[]{ 0.33, 0.33, 1, 1, 1, 0.0, 0.25 });

        return custom;
    }


    public static void main(String[] args) {
        // RNG for various random no. selections
        Random random = new Random();

        // Create population, geneLength and size must be > 1 at all times, I won't bother with error checking.
        // Replace createCustomGene() argument with null to test with random data (no preferences) instead of same data.
        // Final 2 arrays represent Persons to be grouped together and split up respectively.
        Population population = Population.initialise(GENE_LENGTH, POPULATION_SIZE, GROUP_NUMBER, createCustomGene(), new int[]{}, new int[]{});
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