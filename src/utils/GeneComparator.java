package utils;

import geneticsteps.Gene;

import java.util.Comparator;

/**
 * Class to sort Population gene field, based on individual gene fitness, in ascending order. Used only in method for finding fittest n genes.
 */
public class GeneComparator implements Comparator<Gene> {

    @Override
    public int compare(Gene g1, Gene g2) {
        return g2.getFitness() - g1.getFitness();
    }
}
