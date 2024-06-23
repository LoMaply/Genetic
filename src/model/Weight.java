package model;

import java.util.Arrays;

/**
 * Stores weights of various characteristics.
 */
public class Weight {

    // Absolute weights of individual characteristics.
    // Weight of Heterogeneous characteristics.
    public static final double GENDER = 1;
    public static final double ADAPTABILITY = 1;
    public static final double COMMS_STYLE_PREF = 1; // Preferred communication style (Assert/Collab/Avoid)
    public static final double COMMS_INITIATE_COMFORT = 1;
    public static final double COMMS_INITIATE_FREQ = 1;
    public static final double MANAGE_MISTAKE = 1;
    public static final double MANAGE_CONFLICT = 1;

    // Weight of Homogeneous characteristics.
    public static final double BIRD_OWL = 1; // Early bird or night owl
    public static final double MEETING_TIME = 1; // Morning/Evening meeting
    public static final double DOER_PLANNER = 1 ;
    public static final double MANAGE_DEADLINE = 1;
    public static final double WORK_CONSISTENCY = 1;
    public static final double WORK_COMMITMENT = 1;
    public static final double MEETING_LOCATION = 1;
    public static final double MEETING_FREQ = 1;
    public static final double COMMS_EFFECTIVE_IMPT = 1; // Importance of effective communication for respondent
    public static final double PROJECT_PREF = 1;
    public static final double EXPLORE = 1;


    // Weight of feedback give/receive cohesiveness. (TEST)
    public static final double COHESIVENESS = 1;


    // Weights of characteristics stored in array.
    // No of items in each array determines length of hetero/homo arrays in Person object.
    public static double[] heteroWeights = new double[]{ GENDER, ADAPTABILITY, COMMS_STYLE_PREF, COMMS_INITIATE_COMFORT, COMMS_INITIATE_FREQ, MANAGE_MISTAKE, MANAGE_CONFLICT };
    public static double heteroWeightSum = Arrays.stream(heteroWeights).sum();
    public static int HETERO_TOTAL_COUNT = heteroWeights.length;
    public static double[] homoWeights = new double[] { BIRD_OWL, MEETING_TIME, DOER_PLANNER, MANAGE_DEADLINE, WORK_CONSISTENCY, WORK_COMMITMENT, MEETING_LOCATION, MEETING_FREQ, COMMS_EFFECTIVE_IMPT, PROJECT_PREF, EXPLORE };
    public static double homoWeightSum = Arrays.stream(homoWeights).sum();
    public static int HOMO_TOTAL_COUNT = homoWeights.length;


    // Weights of F values for calculating overall fitness.
    // Weights of fHetero and fHomo (specifically used in calculating fMix). Total should be 1.
    public static final double WEIGHT_HETEROGENEOUS = 0.3;
    public static final double WEIGHT_HOMOGENEOUS = 1 - WEIGHT_HETEROGENEOUS;

    // Weights of fMix, fBal, fDist and fPref
    public static final double WEIGHT_MIX = 1;
    public static final double WEIGHT_BALANCE = 1;
    public static final double WEIGHT_DISTRIBUTION = 0;
    public static final double WEIGHT_PREFERENCE = 0;
    public static final double F_TOTAL_WEIGHT = WEIGHT_MIX + WEIGHT_BALANCE + WEIGHT_DISTRIBUTION + WEIGHT_PREFERENCE;
}
