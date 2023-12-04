package geneticsteps;

import model.Person;
import model.Weight;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gene {

    public static List<Person> baseGene;
    public static int[] groupIndex;
    private final Person[] gene;
    private final double fitness;
    private final int length;

    /**
     * Constructor for Gene, only called when the initial population is being built.
     */
    public Gene(int length) {
        this.gene = getShuffledBase();
        this.length = length;
        this.fitness = calculateFitness(gene);
    }

    /**
     * Overloaded constructor for Gene, called when generating child genes during Crossover.
     */
    public Gene(Person[] gene, int length) {
        this.gene = gene;
        this.length = length;
        this.fitness = calculateFitness(gene);
    }

    /**
     * Creates an array of Person objects, to be used for generating random permutations for initial population.
     * @param geneLength No. of Person objects in each gene.
     * @param groupNo No. of equal sized groups to form.
     */
    public static void setBaseGene(int geneLength, int groupNo) {
        baseGene = Arrays.asList(Person.createPeople(geneLength));

        // Stores 1st index of each group in groupIndex.
        groupIndex = new int[groupNo];
        int index = 0;
        int standard = geneLength / groupNo;
        if (geneLength % groupNo == 0) { // No remainder
            for (int i = 0; i < groupNo; i++) {
                groupIndex[i] = index;
                index += standard;
            }
        } else { // Remainder split evenly to groups starting from back
            int zp = groupNo - (geneLength % groupNo);
            for (int i = 0; i < groupNo; i++) {
                groupIndex[i] = index;
                index += standard;
                if (i >= zp) {
                    index++;
                }
            }
        }
    }

    /**
     * Returns a shuffled copy of the baseGene.
     */
    public static Person[] getShuffledBase() {
        Collections.shuffle(baseGene);
        return baseGene.toArray(Person[]::new);
    }

    /**
     * WIP fitness calculation function for prototype algorithm.
     * Currently only considers Homogeneous and Heterogeneous charateristics.
     * @param gene Person array to calculate fitness for.
     * @return Fitness value as a double.
     */
    public static double calculateFitness(Person[] gene) {
        double heteroTotal = 0;
        double homoTotal = 0;
        // For each group based on groupIndex
        for (int i = 0; i < groupIndex.length; i++) {
            // Index of first group member
            int firstMem = groupIndex[i];
            // Index of last group member + 1
            int lastMemEx = i < groupIndex.length - 1 ? groupIndex[i + 1] : gene.length;

            // For each possible pair in group, compute Similarity and add to total.
            for (int j = firstMem; j < lastMemEx - 1; j++) {
                for (int k = j + 1; k < lastMemEx; k++) {
                    heteroTotal += gene[j].calcSimilarity(gene[k]);
                    homoTotal += gene[j].calcDifference(gene[k]);
                }
            }
        }

        return 1 / ((Weight.WEIGHT_HETEROGENEOUS) * heteroTotal + (Weight.WEIGHT_HOMOGENEOUS) * homoTotal);
    }

    /**
     * Crossover operation to create children using randomly generated indexes {@param start} and {@param end}.
     * @param parent2 Second parent to Crossover with.
     */
    public Gene crossParent(Gene parent2, int start, int end) {
        Person[] child = new Person[this.length];

        // Copies selected portion from parent1 to child
        System.arraycopy(this.gene, start, child, start, end - start + 1);

        // Create map between values of selected portion
        Map<Person, Person> mapping = new HashMap<>();
        for (int i = start; i <= end; i++) {
            mapping.put(this.gene[i], parent2.gene[i]);
        }

        // Validates child
        for (int i = 0; i < this.length; i++) {
            if (i < start || i > end) {
                Person value = parent2.gene[i];
                while (mapping.containsKey(value)) {
                    value = mapping.get(value);
                }
                child[i] = value;
            }
        }
        return new Gene(child, length);
    }

    /**
     * Performs swap mutation using randomly generated indexes {@param start} and {@param end}.
     */
    public Gene mutateSwap(int random1, int random2) {
        Person temp = gene[random1];
        gene[random1] = gene[random2];
        gene[random2] = temp;
        return this;
    }

    /**
     * Performs invert mutation, with condition to only apply when result is fitter than input.
     * Uses randomly generated indexes {@param start} and {@param end}.
     */
    public Gene mutateInvert(int random1, int random2) {
        int left = Math.min(random1, random2);
        int right = Math.max(random1, random2);

        Person[] result = Arrays.copyOf(this.gene, this.length);

        while (left < right) {
            Person temp = result[left];
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
    public double getFitness() {
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
