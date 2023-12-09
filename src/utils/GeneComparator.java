package utils;

import geneticsteps.Gene;

import java.util.Comparator;

/**
 * Sort Population by individual gene fitness, in descending order.
 * Used to find n fittest genes by taking first n elements of sorted population.
 */
public class GeneComparator implements Comparator<Gene> {

    @Override
    public int compare(Gene g1, Gene g2) {
        // Temporary solution as fitness is a double
        double result = g2.getFitness() - g1.getFitness();
        return result == 0 ? 0 : (result < 0 ? -1 : 1);
    }
}
