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

                    if (!selectedGenes.contains(gene)) { // Ensures each gene only chosen once
                        selectedGenes.add(gene);
                    }
                    pointer += spacing;
                }
            }
        }
        return selectedGenes;
    }
}
