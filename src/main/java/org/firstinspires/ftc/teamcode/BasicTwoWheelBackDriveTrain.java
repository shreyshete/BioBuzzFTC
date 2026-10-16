/* Copyright (c) 2017 FIRST. All rights reserved. */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;

public class BasicTwoWheelBackDriveTrain {

    // Encoder counts per revolution for a goBILDA 312 RPM motor.
    // Adjust to match your actual motor if different.
    private static final double COUNTS_PER_MOTOR_REV  = 537.7;
    private static final double WHEEL_DIAMETER_INCHES  = 4.0;
    private static final double COUNTS_PER_INCH =
            COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * Math.PI);

    // Distance between the center of the left and right wheels (inches).
    // Measure this on your actual robot.
    private static final double TRACK_WIDTH_INCHES = 14.0;

    private ElapsedTime runtime   = new ElapsedTime();
    private DcMotor     backLeft  = null;
    private DcMotor     backRight = null;
    private Telemetry   telemetry;
    private HardwareMap hardwareMap;

    public void init(Telemetry t, HardwareMap h) {
        telemetry   = t;
        hardwareMap = h;
        telemetry.addData("Status", "Initialized");
        backLeft  = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Reset encoders then run with encoder feedback
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Brake when power is 0 so robot holds position
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        runtime.reset();
    }

    // -------------------------------------------------------------------------
    // TeleOp control
    // -------------------------------------------------------------------------

    public void drive(Gamepad g1) {
        // POV Mode: left stick Y = turn, right stick X = drive
        double turn  = g1.left_stick_y;
        double drive = -g1.right_stick_x;
        double leftPower  = Range.clip(drive + turn, -1.0, 1.0);
        double rightPower = Range.clip(drive - turn, -1.0, 1.0);

        backLeft.setPower(leftPower);
        backRight.setPower(rightPower);
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    /** Direct power control — used by autonomous stop routines. */
    public void setMotorPowers(double leftPower, double rightPower) {
        backLeft.setPower(Range.clip(leftPower,  -1.0, 1.0));
        backRight.setPower(Range.clip(rightPower, -1.0, 1.0));
    }

    // -------------------------------------------------------------------------
    // Encoder-based autonomous movement
    // -------------------------------------------------------------------------

    /**
     * Drive straight using encoders.
     *
     * @param speed  Motor power (0.0 to 1.0).
     * @param inches Distance in inches. Positive = forward, negative = backward.
     */
    public void driveByEncoder(double speed, double inches) {
        int counts = (int) (inches * COUNTS_PER_INCH);

        int leftTarget  = backLeft.getCurrentPosition()  + counts;
        // backRight is physically REVERSE, so its target must be negated
        // relative to its current position to move in the same direction
        int rightTarget = backRight.getCurrentPosition() - counts;

        backLeft.setTargetPosition(leftTarget);
        backRight.setTargetPosition(rightTarget);

        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double absPower = Math.abs(speed);
        backLeft.setPower(absPower);
        backRight.setPower(absPower);

        while (backLeft.isBusy() || backRight.isBusy()) {
            telemetry.addData("Driving",  "%.1f in", inches);
            telemetry.addData("Left",  "%d / %d", backLeft.getCurrentPosition(),  leftTarget);
            telemetry.addData("Right", "%d / %d", backRight.getCurrentPosition(), rightTarget);
        }

        stopAndResetToEncoder();
    }

    /**
     * Turn in place using encoders.
     * Positive degrees = turn right, negative degrees = turn left.
     *
     * @param speed   Motor power (0.0 to 1.0).
     * @param degrees Angle to rotate. Positive = right, negative = left.
     */
    public void turnByEncoder(double speed, double degrees) {
        // Arc each wheel travels = fraction of robot's wheel-track circumference
        double arcInches = (Math.PI * TRACK_WIDTH_INCHES * degrees) / 360.0;
        int counts = (int) (arcInches * COUNTS_PER_INCH);

        // Left forward + right backward = turn right; signs flip for left
        int leftTarget  = backLeft.getCurrentPosition()  + counts;
        int rightTarget = backRight.getCurrentPosition() - counts;

        backLeft.setTargetPosition(leftTarget);
        backRight.setTargetPosition(rightTarget);

        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double absPower = Math.abs(speed);
        backLeft.setPower(absPower);
        backRight.setPower(absPower);

        while (backLeft.isBusy() || backRight.isBusy()) {
            telemetry.addData("Turning", "%.1f deg", degrees);
            telemetry.addData("Left",  "%d / %d", backLeft.getCurrentPosition(),  leftTarget);
            telemetry.addData("Right", "%d / %d", backRight.getCurrentPosition(), rightTarget);
        }

        stopAndResetToEncoder();
    }

    /** Stop motors and restore RUN_USING_ENCODER mode. */
    private void stopAndResetToEncoder() {
        backLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}