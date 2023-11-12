package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gene {

    private final int[] gene;
    private final int fitness;
    private final int length;

    /**
     * Constructor for Gene, only called when the initial population is being built.
     */
    public Gene(int length) {
        this.gene = shuffleArray(length);
        this.length = length;

        int fitness = 1;
        for (int i = 1; i < length; i++) {
            if (gene[i] > gene[i - 1]) {
                fitness++;
            }
        }
        this.fitness = fitness;
    }

    /**
     * Overloaded constructor for Gene, called when generating new genes via Crossover
     */
    public Gene(int[] gene, int length) {
        this.gene = gene;
        this.length = length;
        int fitness = 1;
        for (int i = 1; i < length; i++) {
            if (gene[i] > gene[i - 1]) {
                fitness++;
            }
        }
        this.fitness = fitness;
    }

    /**
     * Returns a randomly shuffled array containing numbers 1 to {@param size}.
     * Used to create a randomised gene at the start.
     */
    public static int[] shuffleArray(int size) {
        List<Integer> gene = IntStream.range(0, size).boxed().collect(Collectors.toList());
        Collections.shuffle(gene);
        return gene.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Crossover operation to create children.
     */
    public Gene crossParent(Gene parent2, int start, int end) {
        int[] child = new int[this.length];

        // Copies selected portion from parent1 to child
        System.arraycopy(this.gene, start, child, start, end - start + 1);

        // Create map between values of selected portion
        Map<Integer, Integer> mapping = new HashMap<>();
        for (int i = start; i <= end; i++) {
            mapping.put(this.gene[i], parent2.gene[i]);
        }

        // Validates child
        for (int i = 0; i < this.length; i++) {
            if (i < start || i > end) {
                int value = parent2.gene[i];
                while (mapping.containsKey(value)) {
                    value = mapping.get(value);
                }
                child[i] = value;
            }
        }
        return new Gene(child, length);
    }

    /**
     * Performs swap mutation.
     */
    public void mutateSwap(int random1, int random2) {
        int temp = gene[random1];
        gene[random1] = gene[random2];
        gene[random2] = temp;
    }

    public void mutateInvert() {

    }

    /**
     * Returns fitness of Gene.
     */
    public int getFitness() {
        return this.fitness;
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
