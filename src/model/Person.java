package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Person {
    private static int global = 0;

    private final int id;
    private final List<Integer> preferences; // List of id's of other people current Person object prefers to be matched with.
    private final double[] heterogeneous;
    private final double[] homogeneous;

    /**
     * Creates a Person with random characteristics. Will allow for non-random characteristics in the future.
     * No. of characteristics from each group depends on Weight class.
     */
    private Person() {
        Random random = new Random();
        this.id = global++;
        this.preferences = new ArrayList<>();
        this.heterogeneous = Arrays.stream(new double[Weight.heteroWeights.length]).map(x -> random.nextFloat()).toArray();
        this.homogeneous = Arrays.stream(new double[Weight.homoWeights.length]).map(x -> random.nextFloat()).toArray();
    }

    public Person(int[] pref, double[] hetero, double[] homo) {
        this.id = global++;
        this.preferences = Arrays.stream(pref).boxed().collect(Collectors.toList());
        this.heterogeneous = hetero;
        this.homogeneous = homo;
    }

    /**
     * Creates {@param length} randomly generated People objects with no preferences.
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
     * Calculates (1 - average) of {@param pair1}'s and {@param pair2}'s preference for each other.
     * Currently, each Person's peference for another Person is ether 0 or 1. Might be changed to support 0 <= x <= 1 in future.
     * @return Preference average for fPref calculation.
     */
    public static double isPreferred(Person pair1, Person pair2) {
        int first = pair1.preferences.contains(pair2.id) ? 1 : 0;
        int second = pair2.preferences.contains(pair1.id) ? 1 : 0;
        return 1 - ((first + second) / 2.0);
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
