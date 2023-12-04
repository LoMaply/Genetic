package model;

import java.util.Random;

public class Person {
    private static int global = 0;
    private int id;
    private double[] heterogeneous;
    private double[] homogeneous;

    /**
     * Creates a Person with random characteristics.
     * Each characteristic group contains 2 characteristics, each with a value of 0 < x < 1.
     */
    private Person() {
        Random random = new Random();
        this.id = global++;
        this.heterogeneous = new double[]{ random.nextFloat(), random.nextFloat(), random.nextFloat() };
        this.homogeneous = new double[]{ random.nextFloat(), random.nextFloat(), random.nextFloat() };
    }

    /**
     * Creates a {@code length} randomly generated People objects.
     * @return People objects returned as an array.
     */
    public static Person[] createPeople(int length) {
        Person[] result = new Person[length];
        for (int i = 0; i < length; i++) {
            result[i] = new Person();
        }
        return result;
    }

    @Override
    public String toString() {
        return Integer.toString(this.id);
        // return this.id + "\n" + Arrays.toString(heterogeneous);
    }

    /**
     * Calculates similarity for 2 Person objects based on Heterogeneous characteristics.
     * @param groupMate 2nd Person object to be calculated with.
     * @return Similarity value, as a double.
     */
    public double calcSimilarity(Person groupMate) {
        double[] first = this.heterogeneous;
        double[] second = groupMate.heterogeneous;
        double similaritySum = 0;
        for (int i = 0; i < first.length; i++) {
            similaritySum += Weight.heteroWeights[i] * Math.abs(first[i] - second[i]);
        }
        return similaritySum / Weight.heteroWeightSum;
    }

    /**
     * Calculates difference for 2 Person objects based on Homogeneous characteristics.
     * @param groupMate 2nd Person object to be calculated with.
     * @return Difference value, as a double.
     */
    public double calcDifference(Person groupMate) {
        double[] first = this.homogeneous;
        double[] second = groupMate.homogeneous;
        double differenceSum = 0;
        for (int i = 0; i < first.length; i++) {
            differenceSum += Weight.homoWeights[i] * Math.abs(first[i] - second[i]);
        }
        return 1 - (differenceSum / Weight.homoWeightSum);
    }


}
