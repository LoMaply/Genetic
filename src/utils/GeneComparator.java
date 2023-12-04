package utils;

import geneticsteps.Gene;

import java.util.Comparator;

/**
 * Class to sort Population gene field, based on individual gene fitness, in ascending order. Used only in method for finding fittest n genes.
 */
public class GeneComparator implements Comparator<Gene> {

    @Override
    public int compare(Gene g1, Gene g2) {
        // Temporary solution as fitness is a double
        double result = g2.getFitness() - g1.getFitness();
        return result == 0 ? 0 : (result < 0 ? -1 : 1);
    }
}
