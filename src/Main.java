import model.Population;

public class Main {

    public static int POPULATION_SIZE = 20; // Size of population in each generation
    public static double GENERATION_GAP = 0.6; // Ratio of parent population to child population, must be 0 < x < 1. POPULATION_SIZE * GENERATION_GAP = no. of child genes needed. Remaining space is saved for fittest parents.
    public static int OFFSPRING_COUNT = getOffspringCount();

    /**
     * Calculates no. of offspring to generate based on POPULATION_SIZE & GENERATION_GAP, that is a multiple of 2.
     */
    public static int getOffspringCount() {
        int count = (int) (POPULATION_SIZE * GENERATION_GAP);
        return (count % 2) == 0 ? count : count + 1;
    }

    public static void main(String[] args) {


        // Create population, geneLength and size must be > 1 at all times, I won't bother with error checking.
        Population test = new Population(30, POPULATION_SIZE);


        test.printPopulation();
    }
}