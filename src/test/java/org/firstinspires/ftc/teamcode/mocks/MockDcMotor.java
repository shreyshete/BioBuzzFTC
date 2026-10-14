package org.firstinspires.ftc.teamcode.mocks;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Mock DcMotor that records state for assertions and simulates encoder
 * position advancing proportionally to target position so isBusy() resolves.
 */
public class MockDcMotor implements DcMotor {

    // Inspectable state
    public double    lastPower       = 0.0;
    public Direction lastDirection   = Direction.FORWARD;
    public RunMode   lastMode        = RunMode.RUN_WITHOUT_ENCODER;
    public int       targetPosition  = 0;
    public int       currentPosition = 0;
    public ZeroPowerBehavior lastZeroPowerBehavior = ZeroPowerBehavior.FLOAT;

    // ---------------------------------------------------------------- power

    @Override public void   setPower(double power)  { this.lastPower = power; }
    @Override public double getPower()               { return lastPower; }

    // ---------------------------------------------------------------- direction

    @Override public void      setDirection(Direction direction) { this.lastDirection = direction; }
    @Override public Direction getDirection()                    { return lastDirection; }

    // ---------------------------------------------------------------- mode

    @Override
    public void setMode(RunMode mode) {
        this.lastMode = mode;
        if (mode == RunMode.STOP_AND_RESET_ENCODER) {
            currentPosition = 0;
            targetPosition  = 0;
        }
        // When RUN_TO_POSITION is set, immediately "arrive" at target so
        // isBusy() returns false after the first check in tests.
        if (mode == RunMode.RUN_TO_POSITION) {
            currentPosition = targetPosition;
        }
    }

    @Override public RunMode getMode() { return lastMode; }

    // ---------------------------------------------------------------- encoder

    @Override public void    setTargetPosition(int position) { this.targetPosition = position; }
    @Override public int     getTargetPosition()             { return targetPosition; }
    @Override public int     getCurrentPosition()            { return currentPosition; }

    /**
     * Returns true if the motor has not yet reached its target.
     * In tests, setMode(RUN_TO_POSITION) snaps currentPosition to target,
     * so isBusy() returns false immediately — keeping tests fast.
     */
    @Override public boolean isBusy() { return currentPosition != targetPosition; }

    // ---------------------------------------------------------------- zero power

    @Override public void              setZeroPowerBehavior(ZeroPowerBehavior b) { this.lastZeroPowerBehavior = b; }
    @Override public ZeroPowerBehavior getZeroPowerBehavior()                    { return lastZeroPowerBehavior; }
}
