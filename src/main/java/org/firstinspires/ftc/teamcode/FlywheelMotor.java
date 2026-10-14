/* Copyright (c) 2017 FIRST. All rights reserved. */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;

public class FlywheelMotor {

    private ElapsedTime runtime    = new ElapsedTime();
    private DcMotor     flyWheel   = null;
    private Telemetry   telemetry;
    private HardwareMap hardwareMap;

    // Toggle state
    private boolean flywheelOn  = false;
    private boolean bWasPressed = false;

    public void init(Telemetry t, HardwareMap h) {
        telemetry   = t;
        hardwareMap = h;
        telemetry.addData("Status", "Initialized");
        flyWheel = hardwareMap.get(DcMotor.class, "flyWheel");
        flyWheel.setDirection(DcMotor.Direction.FORWARD);
        runtime.reset();
    }

    public void spinFlywheel(Gamepad g) {
        // Rising-edge detection: only toggle on the first frame B is pressed
        boolean bIsPressed = g.b;
        if (bIsPressed && !bWasPressed) {
            flywheelOn = !flywheelOn;
        }
        bWasPressed = bIsPressed;

        double flyPower = flywheelOn ? 1.0 : 0.0;
        flyWheel.setPower(flyPower);
        telemetry.addData("Flywheel", flywheelOn ? "ON  (1.0)" : "OFF (0.0)");
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /** Direct power control for autonomous use. */
    public void setFlywheelPower(double power) {
        flywheelOn = (power != 0.0);
        flyWheel.setPower(Range.clip(power, -1.0, 1.0));
    }
}