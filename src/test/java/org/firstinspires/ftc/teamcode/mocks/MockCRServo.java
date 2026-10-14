package org.firstinspires.ftc.teamcode.mocks;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Mock CRServo that records the last power set on it.
 */
public class MockCRServo implements CRServo {

    public double lastPower = 0.0;
    public DcMotorSimple.Direction lastDirection = DcMotorSimple.Direction.FORWARD;

    @Override
    public void setPower(double power) {
        this.lastPower = power;
    }

    @Override
    public double getPower() {
        return lastPower;
    }

    @Override
    public void setDirection(DcMotorSimple.Direction direction) {
        this.lastDirection = direction;
    }

    @Override
    public DcMotorSimple.Direction getDirection() {
        return lastDirection;
    }
}
