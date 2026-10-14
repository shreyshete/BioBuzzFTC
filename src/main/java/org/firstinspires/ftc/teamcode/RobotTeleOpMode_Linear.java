/* Copyright (c) 2017 FIRST. All rights reserved. */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="RobotTeleOpMode_Linear", group="Linear OpMode")
public class RobotTeleOpMode_Linear extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // gamepad1 = driver  (drivetrain sticks + A/B/X/Y presets)
    // gamepad2 = operator (A = ball pusher | B toggle = flywheel | X toggle = intake)
    private BallPusherServo             ballPusherServo             = new BallPusherServo();
    private BasicTwoWheelBackDriveTrain basicTwoWheelBackDriveTrain = new BasicTwoWheelBackDriveTrain();
    private FlywheelMotor               flywheelMotor               = new FlywheelMotor();
    private MasterIntakeMotor           masterIntakeMotor           = new MasterIntakeMotor();

    // Rising-edge trackers for gamepad1 preset buttons
    private boolean aWasPressed = false;
    private boolean bWasPressed = false;
    private boolean xWasPressed = false;
    private boolean yWasPressed = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ballPusherServo.init(telemetry, hardwareMap);
        basicTwoWheelBackDriveTrain.init(telemetry, hardwareMap);
        flywheelMotor.init(telemetry, hardwareMap);
        masterIntakeMotor.init(telemetry, hardwareMap);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // --- Normal driver controls (sticks) ---
            basicTwoWheelBackDriveTrain.drive(gamepad1);

            // --- Operator controls ---
            ballPusherServo.spinBallPusher(gamepad2);   // A
            flywheelMotor.spinFlywheel(gamepad2);       // B toggle
            masterIntakeMotor.spintake(gamepad2);       // X toggle

            // --- Gamepad1 preset routines (rising-edge only) ---
            runPresets();

            telemetry.update();
        }
    }

    /**
     * Checks gamepad1 face buttons and fires a preset routine on the first
     * frame each button is pressed.  The robot pauses normal driving while
     * the routine runs, then resumes.
     */
    private void runPresets() {

        // A -- 
        if (gamepad1.a && !aWasPressed) {
            presetA();
        }

        // B -- placeholder: add your routine here
        if (gamepad1.b && !bWasPressed) {
            presetB();
        }

        // X -- placeholder: add your routine here
        if (gamepad1.x && !xWasPressed) {
            presetX();
        }

        // Y -- placeholder: add your routine here
        if (gamepad1.y && !yWasPressed) {
            presetY();
        }

        // Update rising-edge trackers
        aWasPressed = gamepad1.a;
        bWasPressed = gamepad1.b;
        xWasPressed = gamepad1.x;
        yWasPressed = gamepad1.y;
    }

    // -------------------------------------------------------------------------
    // Preset routines -- fill in B, X, Y as needed
    // -------------------------------------------------------------------------

    /** Gamepad1 A -- drive forward 24 inches at 60% power. */
    private void presetA() {
        telemetry.addData("Preset", "A: driving forward 24 in");
        telemetry.update();
        basicTwoWheelBackDriveTrain.driveByEncoder(0.6, 24);
    }

    /** Gamepad1 B -- add your routine here. */
    private void presetB() {
        telemetry.addData("Preset", "B: not yet assigned");
        telemetry.update();
        // TODO: add your B preset
    }

    /** Gamepad1 X -- add your routine here. */
    private void presetX() {
        telemetry.addData("Preset", "X: not yet assigned");
        telemetry.update();
        // TODO: add your X preset
    }

    /** Gamepad1 Y -- add your routine here. */
    private void presetY() {
        telemetry.addData("Preset", "Y: not yet assigned");
        telemetry.update();
        // TODO: add your Y preset
    }
}