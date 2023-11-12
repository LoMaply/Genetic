package geneticsteps;

import model.Gene;
import model.Population;

import java.util.ArrayList;
import java.util.List;

public class Stochastic {

    /**
     * Performs SUS on {@param population} to select {@param totalSelections} Genes.
     */
    public static List<Gene> selectGenes(Population population, int totalSelections) {
        List<Gene> selectedGenes = new ArrayList<>();
        double totalFitness = population.getTotalFitness();
        double spacing = totalFitness / totalSelections;
        double pointer = Math.random() * spacing;
        double cumulativeProbability = 0;

        ArrayList<Gene> currentPopulation = population.getGenes();

        while (selectedGenes.size() < totalSelections) {
            for (Gene gene : currentPopulation) {
                cumulativeProbability += gene.getFitness();
                while (cumulativeProbability > pointer) {

                    selectedGenes.add(gene);

                    // At first I had this condition to prevent the same gene from being chosen multiple times
                    // But it led to a near infinite loop in some cases
                    // Still seems to work even when removing this condition

                    /*
                    if (!selectedGenes.contains(gene)) {
                        selectedGenes.add(gene);
                    }
                    */
                    pointer += spacing;
                }
            }
        }
        return selectedGenes;
    }
}
