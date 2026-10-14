package com.qualcomm.robotcore.hardware;

/**
 * Stub for FTC DcMotorSimple — base interface for DC motors and CR servos.
 */
public interface DcMotorSimple {
    enum Direction { FORWARD, REVERSE }

    void setDirection(Direction direction);
    Direction getDirection();
    void setPower(double power);
    double getPower();
}
