package com.qualcomm.robotcore.util;

/**
 * Stub for FTC Range utility — matches the real SDK's static clip() signatures.
 */
public class Range {
    /**
     * Clip a double value to [min, max].
     */
    public static double clip(double number, double min, double max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    /**
     * Clip a float value to [min, max].
     */
    public static float clip(float number, float min, float max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    /**
     * Clip an int value to [min, max].
     */
    public static int clip(int number, int min, int max) {
        if (number < min) return min;
        if (number > max) return max;
        return number;
    }

    // Prevent instantiation
    private Range() {}
}
