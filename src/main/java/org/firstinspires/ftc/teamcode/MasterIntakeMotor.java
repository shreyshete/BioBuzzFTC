/* Copyright (c) 2017 FIRST. All rights reserved. */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;

public class MasterIntakeMotor{

    private ElapsedTime runtime    = new ElapsedTime();
    private DcMotor     intake = null;
    private Telemetry   telemetry;
    private HardwareMap hardwareMap;

    // Toggle state
    private boolean intakeOn  = false;
    private boolean xWasPressed = false;

    public void init(Telemetry t, HardwareMap h) {
        telemetry   = t;
        hardwareMap = h;
        telemetry.addData("Status", "Initialized");
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        runtime.reset();
    }

    public void spintake(Gamepad g) {
        // Rising-edge detection: only toggle on the first frame X is pressed
        boolean xIsPressed = g.x;
        if (xIsPressed && !xWasPressed) {
            intakeOn = !intakeOn;
        }
        xWasPressed = xIsPressed;

        double intakePower = intakeOn ? 1.0 : 0.0;
        intake.setPower(intakePower);
        telemetry.addData("Intake", intakeOn ? "ON  (1.0)" : "OFF (0.0)");
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /** Direct power control for autonomous use. */
    public void setIntakePower(double power) {
        intakeOn = (power != 0.0);
        intake.setPower(Range.clip(power, -1.0, 1.0));
    }
}