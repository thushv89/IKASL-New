package com.msc.utils;

import com.msc.enums.GenType;

public class AlgoParameters {

    public static int DIMENSIONS;
    private static double GT;
    public static double SPREAD_FACTOR;
    public static double MAX_NEIGHBORHOOD_RADIUS;
    private static double FD;
    public static double START_LEARNING_RATE;
    public static int MAX_ITERATIONS;
    
    public static int LEARN_CYCLES;
    public static GenType gType;
    public static int HIT_THRESHOLD;

    public static double getGT() {
        GT = -DIMENSIONS * DIMENSIONS * Math.log(SPREAD_FACTOR);
        return GT;
    }

    public static double getFD() {
        FD = SPREAD_FACTOR / DIMENSIONS;
        return FD;
    }
}
