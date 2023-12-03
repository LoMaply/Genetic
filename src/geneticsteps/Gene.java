package geneticsteps;

import java.util.Arrays;
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
        this.fitness = calculateFitness(gene);
    }

    /**
     * Overloaded constructor for Gene, called when generating child genes during Crossover.
     */
    public Gene(int[] gene, int length) {
        this.gene = gene;
        this.length = length;
        this.fitness = calculateFitness(gene);
    }

    /**
     * Placeholder fitness calculation function for prototype algorithm.
     */
    public static int calculateFitness(int[] gene) {
        int fitness = 1;
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == i) { // Change this definition
                fitness++;
            }
        }
        return fitness;
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
    public Gene mutateSwap(int random1, int random2) {
        int temp = gene[random1];
        gene[random1] = gene[random2];
        gene[random2] = temp;
        return this;
    }

    /**
     * Performs invert mutation, with condition to only apply when result is fitter than input.
     */
    public Gene mutateInvert(int random1, int random2) {
        int left = Math.min(random1, random2);
        int right = Math.max(random1, random2);

        int[] result = Arrays.copyOf(this.gene, this.length);

        while (left < right) {
            int temp = result[left];
            result[left] = result[right];
            result[right] = temp;

            left++;
            right--;
        }
        return (calculateFitness(result) > calculateFitness(this.gene)) ? new Gene(result, this.length) : this;
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
