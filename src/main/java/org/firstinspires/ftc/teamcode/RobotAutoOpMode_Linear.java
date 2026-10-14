/* Copyright (c) 2017 FIRST. All rights reserved. */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "RobotAutoOpMode_Linear", group = "Linear OpMode")
public class RobotAutoOpMode_Linear extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Subsystems — same hardware as TeleOp
    private BasicTwoWheelBackDriveTrain drivetrain = new BasicTwoWheelBackDriveTrain();
    private FlywheelMotor               flywheel   = new FlywheelMotor();
    private MasterIntakeMotor           intake     = new MasterIntakeMotor();
    private BallPusherServo             ballPusher = new BallPusherServo();

    @Override
    public void runOpMode() {

        // --- INIT ---
        drivetrain.init(telemetry, hardwareMap);
        flywheel.init(telemetry, hardwareMap);
        intake.init(telemetry, hardwareMap);
        ballPusher.init(telemetry, hardwareMap);

        telemetry.addData("Status", "Ready to start");
        telemetry.update();

        waitForStart();
        runtime.reset();

       //This is good for testing the accuracy and speed of all moving parts
        testRoutine();

        telemetry.addData("Status", "Autonomous complete");
        telemetry.update();
    }

    // -------------------------------------------------------------------------
    // Movement helpers — encoder-based, no guessing on time
    // -------------------------------------------------------------------------

    private void testRoutine() {
        
        driveForward(0.5, 24);    // drive forward 24 inches at 50% power
        turnRight(0.4, 90);       // turn right 90 degrees at 40% power
        turnLeft(0.4, 90);        // turn left 45 degrees at 40% power
        runFlywheel(5.0);  // run flywheel for 3 seconds
        runIntake(5);  // run masterIntake for 5 seconds
        stopAll();

    }

    /**
     * Drive straight a given distance.
     *
     * @param speed  Motor power 0.0 – 1.0.
     * @param inches Distance to travel. Positive = forward, negative = backward.
     */
    private void driveForward(double speed, double inches) {
        drivetrain.driveByEncoder(speed, inches);
    }

    /**
     * Turn right (clockwise) in place by a given number of degrees.
     *
     * @param speed   Motor power 0.0 – 1.0.
     * @param degrees Angle to rotate clockwise.
     */
    private void turnRight(double speed, double degrees) {
        drivetrain.turnByEncoder(speed, Math.abs(degrees));
    }

    /**
     * Turn left (counter-clockwise) in place by a given number of degrees.
     *
     * @param speed   Motor power 0.0 – 1.0.
     * @param degrees Angle to rotate counter-clockwise.
     */
    private void turnLeft(double speed, double degrees) {
        // Negative degrees in turnByEncoder = left turn
        drivetrain.turnByEncoder(speed, -Math.abs(degrees));
    }

    // -------------------------------------------------------------------------
    // Mechanism helpers
    // -------------------------------------------------------------------------

    /**
     * Run the flywheel at full power for the given number of seconds, then stop.
     */
    private void runFlywheel(double seconds) {
        flywheel.setFlywheelPower(1.0);
        sleep((long) (seconds * 1000));
        flywheel.setFlywheelPower(0.0);
    }

    /**
     * Run the intake motor at full power for the given number of seconds, then stop.
     */
    private void runIntake(double seconds) {
        intake.setIntakePower(1.0);
        sleep((long) (seconds * 1000));
        intake.setIntakePower(0.0);
    }

    /**
     * Stop all subsystems immediately.
     */
    private void stopAll() {
        drivetrain.setMotorPowers(0, 0);
        flywheel.setFlywheelPower(0.0);
        intake.setIntakePower(0.0);
        ballPusher.stop();
    }
}