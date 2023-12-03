package model;

import java.util.Random;

public class Person {
    double[] Heterogeneous;
    double[] Homogeneous;
    double[] Balanced;

    /**
     * Creates a Person with random characteristics.
     * Each characteristic group contains 2 characteristics, each with a value of 0 < x < 1.
     */
    public Person() {
        Random random = new Random();
        this.Heterogeneous = new double[]{ random.nextFloat(), random.nextFloat() };
        this.Homogeneous = new double[]{ random.nextFloat(), random.nextFloat() };
        this.Balanced =  new double[]{ random.nextFloat(), random.nextFloat() };
    }

    /**
     * Calculates similarity for 2 Person objects based on Heterogeneous characteristics.
     * @param groupMate 2nd Person object to be calculated with.
     * @return Similarity value, as a double.
     */
    public double calcSimilarity(Person groupMate) {
        double[] first = this.Heterogeneous;
        double[] second = groupMate.Heterogeneous;
        double similaritySum = 0;
        double totalWeightSum = Weight.heteroWeightSum;
        for (int i = 0; i < first.length; i++) {
            similaritySum += Weight.heteroWeights[i] * Math.abs(first[i] - second[i]);
        }
        return similaritySum / totalWeightSum;
    }

    /**
     * Calculates difference for 2 Person objects based on Homogeneous characterustucs.
     * @param groupMate 2nd Person object to be calculated with.
     * @return Difference value, as a double.
     */
    public double calcDifference(Person groupMate) {
        double[] first = this.Homogeneous;
        double[] second = groupMate.Homogeneous;
        double differenceSum = 0;
        double totalWeightSum = Weight.homoWeightSum;
        for (int i = 0; i < first.length; i++) {
            differenceSum += Weight.homoWeights[i] * Math.abs(first[i] - second[i]);
        }
        return 1 - (differenceSum / totalWeightSum);
    }


}
