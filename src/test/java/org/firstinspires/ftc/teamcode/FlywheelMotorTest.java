package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.mocks.MockDcMotor;
import org.firstinspires.ftc.teamcode.mocks.MockTelemetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FlywheelMotor")
class FlywheelMotorTest {

    private FlywheelMotor  flywheel;
    private MockDcMotor    mockMotor;
    private MockTelemetry  telemetry;
    private Gamepad        gamepad2;

    @BeforeEach
    void setUp() {
        mockMotor   = new MockDcMotor();
        telemetry   = new MockTelemetry();
        gamepad2    = new Gamepad();

        HardwareMap hardwareMap = new HardwareMap();
        hardwareMap.put(DcMotor.class, "flyWheel", mockMotor);

        flywheel = new FlywheelMotor();
        flywheel.init(telemetry, hardwareMap);
    }

    @Test
    @DisplayName("starts off — power is 0.0 before any input")
    void startsOff() {
        flywheel.spinFlywheel(gamepad2);
        assertEquals(0.0, mockMotor.lastPower, 1e-6, "Flywheel should be off at start");
    }

    @Test
    @DisplayName("press B once -> flywheel turns on at full power")
    void pressBTurnsOn() {
        gamepad2.b = true;
        flywheel.spinFlywheel(gamepad2);
        assertEquals(1.0, mockMotor.lastPower, 1e-6, "First B press should start flywheel at 1.0");
    }

    @Test
    @DisplayName("press B twice -> flywheel turns back off")
    void pressBTwiceTogglesOff() {
        // First press — on
        gamepad2.b = true;
        flywheel.spinFlywheel(gamepad2);

        // Release
        gamepad2.b = false;
        flywheel.spinFlywheel(gamepad2);

        // Second press — off
        gamepad2.b = true;
        flywheel.spinFlywheel(gamepad2);

        assertEquals(0.0, mockMotor.lastPower, 1e-6, "Second B press should stop flywheel");
    }

    @Test
    @DisplayName("holding B does not keep toggling — only the first frame counts")
    void holdingBDoesNotRetoggle() {
        // Hold B for 5 loop iterations
        gamepad2.b = true;
        for (int i = 0; i < 5; i++) {
            flywheel.spinFlywheel(gamepad2);
        }
        // Should have toggled on exactly once, not 5 times
        assertEquals(1.0, mockMotor.lastPower, 1e-6, "Holding B should only toggle once");
    }

    @Test
    @DisplayName("flywheel stays on across multiple loops without B held")
    void staysOnBetweenLoops() {
        // Press and release B to turn on
        gamepad2.b = true;
        flywheel.spinFlywheel(gamepad2);
        gamepad2.b = false;

        // Several loops with no input
        for (int i = 0; i < 10; i++) {
            flywheel.spinFlywheel(gamepad2);
        }
        assertEquals(1.0, mockMotor.lastPower, 1e-6, "Flywheel should stay on without another B press");
    }

    @Test
    @DisplayName("telemetry shows ON when running")
    void telemetryShowsOn() {
        gamepad2.b = true;
        flywheel.spinFlywheel(gamepad2);
        assertTrue(telemetry.hasEntry("ON"), "Telemetry should say ON when flywheel is running");
    }

    @Test
    @DisplayName("telemetry shows OFF when stopped")
    void telemetryShowsOff() {
        flywheel.spinFlywheel(gamepad2);
        assertTrue(telemetry.hasEntry("OFF"), "Telemetry should say OFF when flywheel is stopped");
    }
}
