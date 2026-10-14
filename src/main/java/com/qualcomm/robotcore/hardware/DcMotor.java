package com.qualcomm.robotcore.hardware;

/**
 * Stub for FTC DcMotor interface — includes encoder and run-mode methods.
 */
public interface DcMotor extends DcMotorSimple {

    enum RunMode {
        RUN_WITHOUT_ENCODER,
        RUN_USING_ENCODER,
        RUN_TO_POSITION,
        STOP_AND_RESET_ENCODER
    }

    enum ZeroPowerBehavior {
        UNKNOWN,
        BRAKE,
        FLOAT
    }

    void setMode(RunMode mode);
    RunMode getMode();

    void setTargetPosition(int position);
    int  getTargetPosition();
    int  getCurrentPosition();
    boolean isBusy();

    void setZeroPowerBehavior(ZeroPowerBehavior behavior);
    ZeroPowerBehavior getZeroPowerBehavior();
}
