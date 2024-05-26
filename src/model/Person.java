package model;

import geneticsteps.Gene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Person {
    private static int global = 0;

    private final int id;
    private final List<Integer> preferences; // List of id's of other people current Person object prefers to be matched with.
    private final double[] heterogeneous;
    private final double[] homogeneous;
    private final double[] feedback;

    public Person(int[] pref, double[] hetero, double[] homo, double[] feedback) {
        this.id = global++;
        this.preferences = Arrays.stream(pref).boxed().collect(Collectors.toList());
        this.heterogeneous = hetero;
        this.homogeneous = homo;
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return Integer.toString(this.id);
        //return this.id + "\n" + Arrays.toString(heterogeneous) + "\n" + Arrays.toString(homogeneous);
    }

    public double[] getHeterogeneous() {
        return heterogeneous;
    }

    public double[] getHomogeneous() {
        return homogeneous;
    }

    /**
     * Calculates similarity for {@param pair1} and {@param pair2} based on Heterogeneous characteristics for fHetero.
     * @return Similarity value, as a double.
     */
    public static double calcSimilarity(Person pair1, Person pair2) {
        double similaritySum = 0;
        for (int i = 0; i < Weight.HOMO_TOTAL_COUNT; i++) {
            similaritySum += Weight.homoWeights[i] * Math.abs(pair1.homogeneous[i] - pair2.homogeneous[i]);
        }
        // Edited here for feedback (TEST)
        similaritySum += Weight.COHESIVENESS * Person.getCohesiveness(pair1, pair2);
        return similaritySum / (Weight.homoWeightSum + Weight.COHESIVENESS);
    }

    /**
     * Maps feedback give/receive between 2 people and returns a lower value if they are move cohesive
     */
    public static double getCohesiveness(Person pair1, Person pair2) {
        int forwardRelation = pair1.feedback[0] == pair2.feedback[1] ? 0 : 1;
        int backwardRelation = pair2.feedback[0] == pair1.feedback[1] ? 0 : 1;
        return forwardRelation + backwardRelation / 2.0;
    }

    /**
     * Calculates difference for {@param pair1} and {@param pair2} based on Homogeneous characteristics for fHomo.
     * @return Difference value, as a double.
     */
    public static double calcDifference(Person pair1, Person pair2) {
        double differenceSum = 0;
        for (int i = 0; i < Weight.HETERO_TOTAL_COUNT; i++) {
            differenceSum += Weight.heteroWeights[i] * Math.abs(pair1.heterogeneous[i] - pair2.heterogeneous[i]);
        }
        return 1 - (differenceSum / Weight.heteroWeightSum);
    }

    /**
     * Calculates (1 - average) of {@param pair1} and {@param pair2} preference for each other for fPref.
     * Currently, each Person's preference for another Person is ether 0 or 1. Might be changed to support 0 <= x <= 1 in future.
     * @return Preference average for fPref calculation.
     */
    public static double calcPreferred(Person pair1, Person pair2) {
        int first = pair1.preferences.contains(pair2.id) ? 1 : 0;
        int second = pair2.preferences.contains(pair1.id) ? 1 : 0;
        return 1 - ((first + second) / 2.0);
    }

    /**
     * Calculates rValue of {@param pair1} and {@param pair2} for fDist.
     * @return rValue as an int.
     */
    public static int calcDistribution(Person pair1, Person pair2) {
        List<Integer> pairId = new ArrayList<>();
        pairId.add(pair1.id);
        pairId.add(pair2.id);
        int rValue = 0;
        if (Gene.aggregatedPersons.containsAll(pairId)) {
            rValue = -1;
        } else if (Gene.distributedPersons.containsAll(pairId)) {
            rValue = 1;
        }
        return rValue;
    }


    public int getId() {
        return this.id;
    }
}
