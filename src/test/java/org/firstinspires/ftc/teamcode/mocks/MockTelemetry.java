package org.firstinspires.ftc.teamcode.mocks;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock Telemetry that records every addData() call.
 * Tests can inspect the log to verify status messages.
 */
public class MockTelemetry implements Telemetry {

    public final List<String> log = new ArrayList<>();
    public int updateCount = 0;

    @Override
    public Item addData(String caption, Object value) {
        log.add(caption + ": " + value);
        return new NoOpItem();
    }

    @Override
    public Item addData(String caption, String format, Object... args) {
        log.add(caption + ": " + String.format(format, args));
        return new NoOpItem();
    }

    @Override
    public void update() {
        updateCount++;
    }

    /** Returns true if any logged entry contains the given text. */
    public boolean hasEntry(String text) {
        return log.stream().anyMatch(e -> e.contains(text));
    }

    private static class NoOpItem implements Item {
        @Override public Item addData(String caption, Object value) { return this; }
    }
}
