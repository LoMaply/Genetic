package geneticsteps;

import java.util.ArrayList;
import java.util.Iterator;
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

                    // Test condition where no duplicates are allowed
                    // Algorithm takes a lot longer to run, a little unsure on effects on the results (if any)
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


    public static List<Gene> selectGenesTest(Population population, int totalSelections) {
        List<Gene> selectedGenes = new ArrayList<>();
        double totalFitness = population.getTotalFitness();
        double spacing = totalFitness / totalSelections;
        double pointer = Math.random() * spacing;
        double cumulativeProbability = 0;

        ArrayList<Gene> currentPopulation = population.getGenes();

        Iterator<Gene> geneIterator = currentPopulation.iterator();
        Gene currentGene = null;

        for (int i = 0; i < totalSelections; i++) {
            while (pointer > cumulativeProbability) {
                if (!geneIterator.hasNext()) {
                    // This should not happen in a properly configured setup, but just in case
                    throw new IllegalStateException("Ran out of genes before completing selection");
                }
                currentGene = geneIterator.next();
                cumulativeProbability += currentGene.getFitness();
            }
            selectedGenes.add(currentGene);
            pointer += spacing;
        }

        return selectedGenes;
    }
}
