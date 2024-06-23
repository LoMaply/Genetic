import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import geneticsteps.Crossover;
import geneticsteps.Gene;
import geneticsteps.Population;
import geneticsteps.Stochastic;
import model.Person;
import model.Weight;
import utils.Pair;


public class Main {

    public static String FILE_LOCATION = "src\\data\\userdata.xlsx";
    public static int POPULATION_SIZE = 50; // No. of genes in each population.
    public static int GENE_LENGTH = 36; // No. of Person objects in each gene.
    public static int GROUP_NUMBER = 5; // No. of equal sized groups to split Person objects into.
    public static int GENERATION_COUNT = 500; // Max no. of generations to run.
    public static double GENERATION_GAP = 0.9; // Ratio of children to parents in next generation, must be 0 < x < 1. POPULATION_SIZE * GENERATION_GAP = no. of child genes needed. Remaining space is saved for fittest parents.
    public static int OFFSPRING_COUNT = getOffspringCount(); // No. of offspring, must be multiple of 2.
    public static double CROSSOVER_PROBABILITY = 0.9; // Chance of crossover operation.
    public static double MUTATION_PROBABILITY = 0.09; // Chance of applying either mutation operations.

    /**
     * Calculates no. of offspring to generate based on POPULATION_SIZE & GENERATION_GAP, ensuring it is a multiple of 2.
     */
    public static int getOffspringCount() {
        int count = (int) (POPULATION_SIZE * GENERATION_GAP);
        return (count % 2) == 0 ? count : count + 1;
    }

    /**
     * Reads and convert user data from excel into Person objects.
     * @return Custom user defined gene.
     */
    public static Person[] createCustomGene() {

        // List<Person> custom = new ArrayList<>(GENE_LENGTH);
        Person[] custom = new Person[GENE_LENGTH];

        try {
            FileInputStream file = new FileInputStream(FILE_LOCATION);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            Sheet heteroData = workbook.getSheetAt(1);
            Sheet homoData = workbook.getSheetAt(2);
            Sheet feedbackData = workbook.getSheetAt(3);

            // i == 1 to ignore headers from excel files
            for (int i = 1; i < GENE_LENGTH + 1; i++) {
                Row hetero = heteroData.getRow(i);
                Row homo = homoData.getRow(i);

                Row feedback = feedbackData.getRow(i);

                double[] heteroChars = convertRowToDoubleArray(hetero, Weight.HETERO_TOTAL_COUNT);
                double[] homoChars = convertRowToDoubleArray(homo, Weight.HOMO_TOTAL_COUNT);

                double[] feedGiveReceive = convertRowToDoubleArray(feedback, 2);

                Person person = new Person(new int[]{}, heteroChars, homoChars, feedGiveReceive);
                //Person person = new Person(new int[]{}, new double[]{1}, new double[]{1}, feedGiveReceive);
                custom[i - 1] = person;
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return custom;
    }

    /**
     * Converts each row of excel data into double array.
     */
    private static double[] convertRowToDoubleArray(Row row, int columnCount) {
        double[] data = new double[columnCount];
        for (int i = 0; i < columnCount; i++) {
            Cell cell = row.getCell(i);
            data[i] = cell.getNumericCellValue();
        }
        return data;
    }

    /**
     * Run genetic algo.
     */
    public static void run() {
        // RNG for various random no. selections
        Random random = new Random();

        // Create population, geneLength and size must be > 1 at all times, I won't bother with error checking.
        // Final 2 arrays represent Persons to be grouped together and split up respectively.
        Population population = Population.initialise(GENE_LENGTH, POPULATION_SIZE, GROUP_NUMBER, createCustomGene(), new int[]{}, new int[]{});
        population.printPopulation();
        System.out.println("\n");

        int count = 0;
        while (count < GENERATION_COUNT) {

            // Stochastic Universal Sampling
            // No. of selected parents = OFFSPRING_COUNT, Every parent pair produces 2 children.
            List<Gene> selectedGenes = Stochastic.selectGenesTest(population, OFFSPRING_COUNT);
            Collections.shuffle(selectedGenes);

            // Used as upper bound when generating random integers to select 2 random points in a gene for Crossover and Mutation Operation
            int limit = GENE_LENGTH - 1;

            // Crossover Operation
            for (int i = 0; i < OFFSPRING_COUNT; i += 2) {
                if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                    Pair<Gene, Gene> children = Crossover.pmxCrossover(selectedGenes.get(i), selectedGenes.get(i + 1), random.nextInt(limit), random.nextInt(limit));
                    selectedGenes.set(i, children.getKey());
                    selectedGenes.set(i + 1, children.getValue());
                }
            }

            // Mutation 1 and 2
            for (int i = 0; i < OFFSPRING_COUNT; i++) {
                // Swap Mutation
                if (random.nextDouble() <= MUTATION_PROBABILITY) {
                    selectedGenes.set(i, selectedGenes.get(i).mutateSwap(random.nextInt(limit), random.nextInt(limit)));
                }
                // Invert Mutation
                if (random.nextDouble() <= MUTATION_PROBABILITY) {
                    selectedGenes.set(i, selectedGenes.get(i).mutateInvert(random.nextInt(limit), random.nextInt(limit)));
                }
            }

            // Elitism (Create new generation using children + fittest parents)
            ArrayList<Gene> childrenPopulation = population.getFittestGenes(POPULATION_SIZE - OFFSPRING_COUNT);
            childrenPopulation.addAll(selectedGenes);
            population.updateGenes(childrenPopulation);

            count++;

            // Printing outputs after each generation
            Gene populationBest = population.getFittestGenes(1).get(0);
            System.out.println("Generation: " + count);
            System.out.println("Fitness of best gene: " + populationBest.getFitness());
            populationBest.printAsGroup();
        }

        population.printPopulation();
        
        Gene fittest = population.getFittestGenes(1).get(0);
        System.out.println("\n\nFinal population fittest gene: " + fittest.toString());
        fittest.printAsGroup();
    }

    public static void main(String[] args) {
        run();
    }
}