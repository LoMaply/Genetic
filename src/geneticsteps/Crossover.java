package geneticsteps;

import utils.Pair;


public class Crossover {

    /**
     * Generates 2 child genes from the 2 input parent genes. Child genes are generated using 2 random array indexes.
     * @return Child genes, as a pair.
     */
    public static Pair<Gene, Gene> pmxCrossover(Gene parent1, Gene parent2, int random1, int random2) {
        int leftbound = Math.min(random1, random2);
        int rightbound = Math.max(random1, random2);

        Gene child1 = parent1.crossParent(parent2, leftbound, rightbound);
        Gene child2 = parent2.crossParent(parent1, leftbound, rightbound);

        return new Pair<>(child1, child2);
    }
}
