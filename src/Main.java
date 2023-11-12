import geneticsteps.Crossover;
import geneticsteps.Stochastic;
import model.Gene;
import model.Population;
import utils.Pair;

import java.util.List;
import java.util.Random;

public class Main {

    public static int POPULATION_SIZE = 20; // No. of genes in each population.
    public static int GENE_LENGTH = 30; // Length of each gene, populates Gene with numbers from 1 to n.
    public static double GENERATION_GAP = 0.9; // Ratio of parent population to child population, must be 0 < x < 1. POPULATION_SIZE * GENERATION_GAP = no. of child genes needed. Remaining space is saved for fittest parents.
    public static double CROSSOVER_PROBABILITY = 0.9; // Chance of crossover operation
    public static int OFFSPRING_COUNT = getOffspringCount(); // No. of offspring, must be multiple of 2.

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

        // Stochastic Universal Sampling
        // No. of selected parents = OFFSPRING_COUNT, Every parent pair produces 2 children.
        List<Gene> selected = Stochastic.selectGenes(population, OFFSPRING_COUNT);
        System.out.println("\n");
        selected.forEach(System.out::println);

        // Crossover Operation
        int size = GENE_LENGTH - 1;
        for (int i = 0; i < OFFSPRING_COUNT; i+=2) {
            if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                Pair<Gene, Gene> children = Crossover.pmxCrossover(selected.get(i), selected.get(i + 1), random.nextInt(size), random.nextInt(size));
                selected.set(i, children.getKey());
                selected.set(i + 1, children.getValue());
            }
        }

        System.out.println("\n");
        selected.forEach(System.out::println);

        // Mutation 1 and 2

    }
}