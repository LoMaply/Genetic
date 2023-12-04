package model;

import java.util.Arrays;

/**
 * Stores weights of various characteristics.
 */
public class Weight {

    // Absolute weights of individual characteristics
    public static final double homo1 = 2;
    public static final double homo2 = 3;
    public static final double homo3 = 5;
    public static final double hetero1 = 2;
    public static final double hetero2 = 5;
    public static final double hetero3 = 10;
    public static final double charWeightSum = homo1 + homo2 + hetero1 + hetero2;



    // Weights of characteristic groups (used for calculating various F values)
    public static final double WEIGHT_HETEROGENEOUS = 0.7;
    public static final double WEIGHT_HOMOGENEOUS = 1 - WEIGHT_HETEROGENEOUS;


    // Normalized weights of characteristics, split into heterogeneous, homogeneous and balanced.
    public static double[] heteroWeights = new double[]{ hetero1, hetero2, hetero3};
    public static double heteroWeightSum = Arrays.stream(heteroWeights).sum();
    public static double[] homoWeights = new double[] { homo1, homo2, homo3 };
    public static double homoWeightSum = Arrays.stream(homoWeights).sum();




}
