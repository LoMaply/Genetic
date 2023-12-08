package geneticsteps;

import model.Person;
import utils.GeneComparator;

import java.util.ArrayList;

public class Population {

    private ArrayList<Gene> genes;
    private final int count;

    /**
     * Creates a population containing {@param size} genes, each of length {@param geneLength}.
     */
    private Population(int geneLength, int size) {
        this.genes = new ArrayList<>(size);
        this.count = size;
        for (int i = 0; i < size; i++) {
            genes.add(new Gene(geneLength));
        }
    }

    /**
     * Creates a base gene and generates initial population by randomly permutating it.
     */
    public static Population initialise(int geneLength, int geneCount, int groupNo, Person[] customGene) {
        Gene.setBaseGene(geneLength, groupNo, customGene);
        return new Population(geneLength, geneCount);
    }

    /**
     * Returns total fitness of population.
     */
    public double getTotalFitness() {
        return genes.stream().map(Gene::getFitness).reduce(0.0, Double::sum);
    }

    /**
     * Returns the ArrayList of Genes.
     */
    public ArrayList<Gene> getGenes() {
        return this.genes;
    }

    /**
     * Returns {@param no} fittest genes from the current population as an ArrayList.
     */
    public ArrayList<Gene> getFittestGenes(int no) {
        genes.sort(new GeneComparator());
        return new ArrayList<>(genes.subList(0, no));
    }

    /**
     * Updates population with new set of genes (for new generations).
     */
    public void updateGenes(ArrayList<Gene> genes) {
        if (genes.size() == count) {
            this.genes = genes;
        }
    }

    /**
     * Prints every gene to the console.
     */
    public void printPopulation() {
        for (int i = 0; i < count; i++) {
            Gene curr = genes.get(i);
            System.out.println((i + 1) + ") " + curr.toString());
            System.out.println("Fitness = " + curr.getFitness());
        }
        System.out.print("Total Fitness = " + getTotalFitness() + "\n");
    }

    /**
     * Only print total fitness of current population.
     */
    public void printTotalFitness() {
        System.out.print("Total Fitness = " + getTotalFitness() + "\n");
    }
}
