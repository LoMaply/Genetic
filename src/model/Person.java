package model;

import java.util.Arrays;
import java.util.Random;

public class Person {
    private static int global = 0;
    private final int id;
    private final double[] heterogeneous;
    private final double[] homogeneous;

    /**
     * Creates a Person with random characteristics. Will allow for non-random characteristics in the future.
     * No. of characteristics from each group depends on Weight class.
     */
    private Person() {
        Random random = new Random();
        this.id = global++;
        this.heterogeneous = Arrays.stream(new double[Weight.heteroWeights.length]).map(x -> random.nextFloat()).toArray();
        this.homogeneous = Arrays.stream(new double[Weight.homoWeights.length]).map(x -> random.nextFloat()).toArray();
    }

    /**
     * Creates {@param length} randomly generated People objects.
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

    public double[] getHeterogeneous() {
        return heterogeneous;
    }

    public double[] getHomogeneous() {
        return homogeneous;
    }

    /**
     * Calculates similarity for 2 Person objects based on Heterogeneous characteristics.
     * @param groupMate 2nd Person object to be calculated with.
     * @return Similarity value, as a double.
     */
    public double calcSimilarity(Person groupMate) {
        double[] first = this.heterogeneous;
        double similaritySum = 0;
        for (int i = 0; i < first.length; i++) {
            similaritySum += Weight.heteroWeights[i] * Math.abs(first[i] - groupMate.heterogeneous[i]);
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
        double differenceSum = 0;
        for (int i = 0; i < first.length; i++) {
            differenceSum += Weight.homoWeights[i] * Math.abs(first[i] - groupMate.homogeneous[i]);
        }
        return 1 - (differenceSum / Weight.homoWeightSum);
    }


}
