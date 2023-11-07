package model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gene {

    private final int[] gene;
    private final int length;

    public Gene(int length) {
        this.gene = shuffleArray(length);
        this.length = length;
    }

    /**
     * Returns a shuffled array containing numbers 1 to {@param size}
     */
    public static int[] shuffleArray(int size) {
        List<Integer> gene = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(gene);
        return gene.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Returns fitness of gene. Fitness here = how many numbers > number in previous index, minimum fitness = 1 for descending array.
     */
    public int getFitness() {
        int fitness = 1;
        for (int i = 1; i < length; i++) {
            if (gene[i] > gene[i - 1]) {
                fitness++;
            }
        }
        return fitness;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < length; i++) {
            result.append(gene[i]).append(" ");
        }
        return result.toString();
    }
}
