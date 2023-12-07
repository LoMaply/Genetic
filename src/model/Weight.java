package model;

import java.util.Arrays;

/**
 * Stores weights of various characteristics.
 */
public class Weight {

    // Absolute weights of individual characteristics.
    // Weight of Homogeneous characteristics.
    public static final double homo1 = 2;
    public static final double homo2 = 3;
    public static final double homo3 = 5;

    // Weight of Heterogeneous characteristics.
    public static final double hetero1 = 2;
    public static final double hetero2 = 5;
    public static final double hetero3 = 10;


    // Weights of characteristics stored in array.
    // No of items in each array determines length of hetero/homo arrays in Person object.
    public static double[] heteroWeights = new double[]{ hetero1, hetero2, hetero3};
    public static double heteroWeightSum = Arrays.stream(heteroWeights).sum();
    public static int HETERO_TOTAL_COUNT = heteroWeights.length;
    public static double[] homoWeights = new double[] { homo1, homo2, homo3 };
    public static double homoWeightSum = Arrays.stream(homoWeights).sum();
    public static int HOMO_TOTAL_COUNT = homoWeights.length;



    // Weights of fHetero and fHomo (specifically used in calculating fMix).
    public static final double WEIGHT_HETEROGENEOUS = 0.7;
    public static final double WEIGHT_HOMOGENEOUS = 1 - WEIGHT_HETEROGENEOUS;

    // Weights of fMix, fBal, fDist and fPref
    public static final double WEIGHT_MIX = 1;
    public static final double WEIGHT_BALANCE = 1;
    public static final double WEIGHT_DISTRIBUTION = 1;
    public static final double WEIGHT_PREFERENCE = 1;
    public static final double F_TOTAL_WEIGHT = WEIGHT_MIX + WEIGHT_BALANCE + WEIGHT_DISTRIBUTION + WEIGHT_PREFERENCE;
    // Temporarily used over F_TOTAL_WEIGHT as fDist and fPref not implemented yet.
    public static final double F_TEMP = WEIGHT_MIX + WEIGHT_BALANCE;

}
