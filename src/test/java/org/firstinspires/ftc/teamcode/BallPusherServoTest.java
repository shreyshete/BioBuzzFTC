package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.mocks.MockCRServo;
import org.firstinspires.ftc.teamcode.mocks.MockTelemetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BallPusherServo — no hardware required.
 *
 * BallPusherServo uses:
 *   hardwareMap.get(CRServo.class, "ballPusher")
 *   gamepad2.a  -> setPower(-1.0)  [reverse / push ball out]
 *   otherwise   -> setPower( 0.0)  [stop]
 */
@DisplayName("BallPusherServo")
class BallPusherServoTest {

    private BallPusherServo servo;
    private MockCRServo     mockServo;
    private MockTelemetry   telemetry;
    private HardwareMap     hardwareMap;
    private Gamepad         gamepad2;

    @BeforeEach
    void setUp() {
        mockServo   = new MockCRServo();
        telemetry   = new MockTelemetry();
        hardwareMap = new HardwareMap();
        gamepad2    = new Gamepad();

        hardwareMap.put(com.qualcomm.robotcore.hardware.CRServo.class, "ballPusher", mockServo);

        servo = new BallPusherServo();
        servo.init(telemetry, hardwareMap);
    }

    // ------------------------------------------------------------------ init

    @Test
    @DisplayName("init() sends Initialized telemetry")
    void initSendsTelemetry() {
        assertTrue(telemetry.hasEntry("Initialized"),
            "Expected 'Initialized' in telemetry after init()");
    }

    // --------------------------------------------------------------- A button

    @Test
    @DisplayName("gamepad2.a pressed -> servo power = -1.0")
    void buttonARunsServoReverse() {
        gamepad2.a = true;
        servo.spinBallPusher(gamepad2);
        assertEquals(-1.0, mockServo.lastPower, 1e-6,
            "Pressing A should set servo power to -1.0 (reverse/push)");
    }

    @Test
    @DisplayName("gamepad2.a released -> servo power = 0.0")
    void buttonAReleasedStopsServo() {
        // First press, then release
        gamepad2.a = true;
        servo.spinBallPusher(gamepad2);

        gamepad2.a = false;
        servo.spinBallPusher(gamepad2);

        assertEquals(0.0, mockServo.lastPower, 1e-6,
            "Releasing A should stop the servo (power = 0.0)");
    }

    @Test
    @DisplayName("no button pressed -> servo power = 0.0")
    void noPressStopsServo() {
        servo.spinBallPusher(gamepad2);
        assertEquals(0.0, mockServo.lastPower, 1e-6,
            "Default state (no buttons) should keep servo stopped");
    }

    // ------------------------------------------------ other buttons (no-ops)

    @Test
    @DisplayName("gamepad2.b pressed -> servo power stays 0.0 (b is not mapped)")
    void buttonBHasNoEffect() {
        gamepad2.b = true;
        servo.spinBallPusher(gamepad2);
        assertEquals(0.0, mockServo.lastPower, 1e-6,
            "Button B is not mapped; servo should remain stopped");
    }

    // --------------------------------------------------------------- telemetry

    @Test
    @DisplayName("spinBallPusher() sends Running telemetry")
    void spinSendsRunningTelemetry() {
        servo.spinBallPusher(gamepad2);
        assertTrue(telemetry.hasEntry("Running"),
            "Expected 'Running' in telemetry during spinBallPusher()");
    }

    // ------------------------------------------ multiple consecutive calls

    @Test
    @DisplayName("toggle: press then release cycles power correctly")
    void togglePowerCycles() {
        for (int i = 0; i < 5; i++) {
            gamepad2.a = (i % 2 == 0);       // alternate press/release
            servo.spinBallPusher(gamepad2);
        }
        // Last iteration: i=4 -> a = true -> power should be -1.0
        assertEquals(-1.0, mockServo.lastPower, 1e-6,
            "After 5 calls ending with a=true, power should be -1.0");
    }
}
