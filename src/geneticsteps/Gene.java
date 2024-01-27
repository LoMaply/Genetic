package geneticsteps;

import model.Person;
import model.Weight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Gene {

    // Class fields to be set before genetic algorithm can proceed.
    private static List<Person> baseGene;
    private static double[] meanHetero; // Mean of each heterogeneous characteristic of all Persons in gene.
    private static double[] meanHomo; // Mean of each homogeneous characteristic of all Persons in gene.
    private static int[] groupIndex; // Starting index of each group in gene.
    public static HashSet<Integer> aggregatedPersons = new HashSet<>(); // Ids of Persons to be grouped together.
    public static HashSet<Integer> distributedPersons = new HashSet<>(); // Ids of Persons to be separated.


    // Fields for Gene instance.
    private final Person[] gene;
    private final double fitness; // Total fitness of gene.
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
    public Gene(Person[] gene, int length, double fitness) {
        this.gene = gene;
        this.length = length;
        this.fitness = fitness;
    }

    /**
     * Creates base array of Person objects, to be used for generating random permutations for initial population.
     * Stores information related to baseGene, useful for some calculations.
     * @param geneLength No. of Person objects to randomly generate OR length of non-null {@param customGene}.
     * @param groupNo No. of equal sized groups to form.
     * @param customGene List of Person objects.
     * @param aggregate Array of Ids of Persons to group together.
     * @param distribute Array of Ids of Persons to separate.
     */
    public static void setBaseInfo(int geneLength, int groupNo, List<Person> customGene, int[] aggregate, int[] distribute) {

        // Set baseGene used to generate initial population.
        baseGene = customGene;

        // Store Ids of aggregated/distributed Persons into respective HashSet (for calculating fDist).
        for (int person : aggregate) {
            aggregatedPersons.add(person);
        }
        for (int person : distribute) {
            distributedPersons.add(person);
        }

        // Store total mean of each characteristic individually (for calculating fBal).
        meanHetero = new double[Weight.HETERO_TOTAL_COUNT];
        meanHomo = new double[Weight.HOMO_TOTAL_COUNT];

        baseGene.forEach(x -> {
            double[] hetero = x.getHeterogeneous();
            double[] homo = x.getHomogeneous();
            for (int i = 0; i < Weight.HETERO_TOTAL_COUNT; i++) {
                meanHetero[i] += (hetero[i] / geneLength);
            }
            for (int i = 0; i < Weight.HOMO_TOTAL_COUNT; i++) {
                meanHomo[i] += (homo[i] / geneLength);
            }
        });

        // Store index of 1st member of each group in groupIndex (for group based calculations).
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
     * Returns a random permutation of baseGene for generating initial population.
     */
    public static Person[] getShuffledBase() {
        Collections.shuffle(baseGene);
        return baseGene.toArray(Person[]::new);
    }

    /**
     * Calculates fitness of gene based on several F values as detailed in reference paper.
     * @param gene Person array to calculate fitness for.
     * @return Fitness value as a double.
     */
    public static double calculateFitness(Person[] gene) {
        double fHetero = 0;
        double fHomo = 0;
        double fBal = 0;
        double fPref = 0;
        double fDist = 0;

        // For each group.
        for (int i = 0; i < groupIndex.length; i++) {

            // Index of first group member
            int firstMem = groupIndex[i];
            // Index of last group member
            int lastMem = i < groupIndex.length - 1 ? groupIndex[i + 1] - 1 : gene.length - 1;

            // For each possible pair in current group.
            for (int j = firstMem; j < lastMem; j++) {
                for (int k = j + 1; k <= lastMem; k++) {
                    // fHetero and fHomo to calculate fMix
                    fHomo += Person.calcSimilarity(gene[j], gene[k]);
                    fHetero += Person.calcDifference(gene[j], gene[k]);

                    fPref += Person.calcPreferred(gene[j], gene[k]);
                    fDist += Person.calcDistribution(gene[j], gene[k]);
                }
            }

            // For fBal.

            double[] groupMeanHetero = new double[Weight.HETERO_TOTAL_COUNT];
            double[] groupMeanHomo = new double[Weight.HOMO_TOTAL_COUNT];

            // Calculate mean char of all members of current group.
            for (int j = firstMem; j <= lastMem; j++) {
                double[] hetero = gene[j].getHeterogeneous();
                double[] homo = gene[j].getHomogeneous();

                for (int k = 0; k < Weight.HETERO_TOTAL_COUNT; k++) {
                    groupMeanHetero[k] += (hetero[k] / gene.length);
                }
                for (int k = 0; k < Weight.HOMO_TOTAL_COUNT; k++) {
                    groupMeanHomo[k] += (homo[k] / gene.length);
                }
            }
            // Summing up for fBal.
            for (int j = 0; j < Weight.HETERO_TOTAL_COUNT; j++) {
                fBal += Math.pow(groupMeanHetero[j] - meanHetero[j], 2);
            }
            for (int j = 0; j < Weight.HOMO_TOTAL_COUNT; j++) {
                fBal += Math.pow(groupMeanHomo[j] - meanHomo[j], 2);
            }
        }

        double fMix = fHetero * Weight.WEIGHT_HETEROGENEOUS + fHomo * Weight.WEIGHT_HOMOGENEOUS;

        // Fitness = 1 / F, hence the inversion of numerator & denominator.
        return Weight.F_TOTAL_WEIGHT / (fMix * Weight.WEIGHT_MIX + fBal * Weight.WEIGHT_BALANCE + fPref * Weight.WEIGHT_PREFERENCE + fDist * Weight.WEIGHT_DISTRIBUTION);
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
        return new Gene(child, length, Gene.calculateFitness(child));
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
        double fitness = calculateFitness(result);
        return (fitness > this.getFitness()) ? new Gene(result, this.length, fitness) : this;
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

    /**
     * Prints gene where all elements are in their respective groups (denoted by a new line).
     */
    public void printAsGroup() {
        System.out.println("Fitness: " + this.fitness);
        for (int i = 0; i < groupIndex.length; i++) {
            int firstMem = groupIndex[i];
            int lastMem = i < groupIndex.length - 1 ? groupIndex[i + 1] - 1 : gene.length - 1;
            ArrayList<Integer> groupList = new ArrayList<>();
            for (int j = firstMem; j <= lastMem; j++) {
                groupList.add(gene[j].getId());
            }
            System.out.println(groupList.stream().sorted().map(String::valueOf).collect(Collectors.joining(" ")));
        }
    }
}
