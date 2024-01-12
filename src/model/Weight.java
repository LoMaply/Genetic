package model;

import java.util.Arrays;

/**
 * Stores weights of various characteristics.
 */
public class Weight {

    // Absolute weights of individual characteristics.
    // Weight of Heterogeneous characteristics.
    public static final double GENDER = 1;
    public static final double MAJOR = 2;
    public static final double DOER_PLANNER = 1; // Do/Plan first
    public static final double ADAPTABILITY = 1;
    public static final double COMMS_PROFFESIONAL = 1;
    public static final double COMMS_GROUP = 1;
    public static final double PROJECT_PREF = 2; // Prefer challenges/projects needing creative/critical thinking?
    public static final double EXPLORE = 2;
    public static final double MANAGE_CONFLICT = 1;

    // Weight of Homogeneous characteristics.
    public static final double FEEDBACK_GIVE = 1;
    public static final double FEEDBACK_GET = 1;
    public static final double BIRD_OWL = 2; // Early bird or night owl
    public static final double MEETING_TIME = 2; // Morning/Evening meeting
    public static final double WORK_CONSISTENCY = 2;
    public static final double MEETING_LOCATION = 2;
    public static final double MEETING_OCCURENCE = 2;

    // Weights of characteristics stored in array.
    // No of items in each array determines length of hetero/homo arrays in Person object.
    public static double[] heteroWeights = new double[]{ GENDER, MAJOR, DOER_PLANNER, ADAPTABILITY, COMMS_PROFFESIONAL, COMMS_GROUP, PROJECT_PREF, EXPLORE, MANAGE_CONFLICT };
    public static double heteroWeightSum = Arrays.stream(heteroWeights).sum();
    public static int HETERO_TOTAL_COUNT = heteroWeights.length;
    public static double[] homoWeights = new double[] { FEEDBACK_GIVE, FEEDBACK_GET, BIRD_OWL, MEETING_TIME, WORK_CONSISTENCY, MEETING_LOCATION, MEETING_OCCURENCE };
    public static double homoWeightSum = Arrays.stream(homoWeights).sum();
    public static int HOMO_TOTAL_COUNT = homoWeights.length;


    // Weights of F values for calculating overall fitness.
    // Weights of fHetero and fHomo (specifically used in calculating fMix). Total should be 1.
    public static final double WEIGHT_HETEROGENEOUS = 0.5;
    public static final double WEIGHT_HOMOGENEOUS = 1 - WEIGHT_HETEROGENEOUS;

    // Weights of fMix, fBal, fDist and fPref
    public static final double WEIGHT_MIX = 2;
    public static final double WEIGHT_BALANCE = 2;
    public static final double WEIGHT_DISTRIBUTION = 0;
    public static final double WEIGHT_PREFERENCE = 0;
    public static final double F_TOTAL_WEIGHT = WEIGHT_MIX + WEIGHT_BALANCE + WEIGHT_DISTRIBUTION + WEIGHT_PREFERENCE;
}
