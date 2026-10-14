package org.firstinspires.ftc.robotcore.external;

/**
 * Stub for FTC Telemetry — records the most recent addData key/value pairs
 * so tests can assert what was sent to the Driver Station.
 */
public interface Telemetry {
    Item addData(String caption, Object value);
    Item addData(String caption, String format, Object... args);
    void update();

    interface Item {
        Item addData(String caption, Object value);
    }
}
