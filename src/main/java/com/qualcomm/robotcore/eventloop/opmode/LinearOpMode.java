package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Stub for FTC LinearOpMode.
 *
 * In tests, subclass this (or use the TestableLinearOpMode helper in the test sources)
 * and override opModeIsActive() to return false after the first loop iteration,
 * so the while-loop in runOpMode() terminates.
 */
public abstract class LinearOpMode {

    /** Simulated telemetry — replaced with a mock in tests. */
    public Telemetry telemetry = new NoOpTelemetry();

    /** Simulated hardware map — populate it in tests before calling runOpMode(). */
    public HardwareMap hardwareMap = new HardwareMap();

    /** Driver (gamepad1 = drivetrain). */
    public Gamepad gamepad1 = new Gamepad();

    /** Operator (gamepad2 = intake/outtake). */
    public Gamepad gamepad2 = new Gamepad();

    /** Override in tests to control how many loop iterations run. */
    public boolean opModeIsActive() {
        return false;
    }

    /** In tests this is a no-op; the test controls when the loop runs. */
    public void waitForStart() {
        // no-op in simulation
    }

    /**
     * Pause execution for the given number of milliseconds.
     * In tests this is an actual sleep; in production the real SDK handles it.
     */
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Override to implement the OpMode logic. */
    public abstract void runOpMode();

    // ------------------------------------------------------------------
    // Private minimal Telemetry implementation so the base class compiles
    // without a mock being injected.
    // ------------------------------------------------------------------
    private static class NoOpTelemetry implements Telemetry {
        @Override public Item addData(String caption, Object value) { return new NoOpItem(); }
        @Override public Item addData(String caption, String format, Object... args) { return new NoOpItem(); }
        @Override public void update() {}

        private static class NoOpItem implements Item {
            @Override public Item addData(String caption, Object value) { return this; }
        }
    }
}
