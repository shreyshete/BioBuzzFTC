package com.qualcomm.robotcore.util;

/**
 * Stub for FTC ElapsedTime — tracks wall-clock time in seconds.
 */
public class ElapsedTime {
    private long startNanos;

    public ElapsedTime() {
        reset();
    }

    public void reset() {
        startNanos = System.nanoTime();
    }

    /** Elapsed time in seconds. */
    public double seconds() {
        return (System.nanoTime() - startNanos) / 1_000_000_000.0;
    }

    /** Elapsed time in milliseconds. */
    public double milliseconds() {
        return (System.nanoTime() - startNanos) / 1_000_000.0;
    }

    @Override
    public String toString() {
        return String.format("%.3f seconds", seconds());
    }
}
