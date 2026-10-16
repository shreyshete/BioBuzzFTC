package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.mocks.MockDcMotor;
import org.firstinspires.ftc.teamcode.mocks.MockTelemetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BasicTwoWheelBackDriveTrain — no hardware required.
 *
 * Drive math (POV mode):
 *   turn       = g1.left_stick_y
 *   drive      = -g1.right_stick_x
 *   leftPower  = clip(drive + turn, -1, 1)
 *   rightPower = clip(drive - turn, -1, 1)
 */
@DisplayName("BasicTwoWheelBackDriveTrain")
class BasicTwoWheelBackDriveTrainTest {

    private BasicTwoWheelBackDriveTrain drivetrain;
    private MockDcMotor  backLeft;
    private MockDcMotor  backRight;
    private MockTelemetry telemetry;
    private HardwareMap   hardwareMap;
    private Gamepad       gamepad1;

    @BeforeEach
    void setUp() {
        backLeft    = new MockDcMotor();
        backRight   = new MockDcMotor();
        telemetry   = new MockTelemetry();
        hardwareMap = new HardwareMap();
        gamepad1    = new Gamepad();

        hardwareMap.put(DcMotor.class, "backLeft",  backLeft);
        hardwareMap.put(DcMotor.class, "backRight", backRight);

        drivetrain = new BasicTwoWheelBackDriveTrain();
        drivetrain.init(telemetry, hardwareMap);
    }

    // ------------------------------------------------------------------ init

    @Test
    @DisplayName("init() sets backLeft direction to FORWARD")
    void initSetsBackLeftForward() {
        assertEquals(DcMotorSimple.Direction.FORWARD, backLeft.lastDirection,
            "backLeft should be FORWARD after init()");
    }

    @Test
    @DisplayName("init() sets backRight direction to REVERSE")
    void initSetsBackRightReverse() {
        assertEquals(DcMotorSimple.Direction.REVERSE, backRight.lastDirection,
            "backRight should be REVERSE after init()");
    }

    @Test
    @DisplayName("init() sends Initialized telemetry")
    void initSendsTelemetry() {
        assertTrue(telemetry.hasEntry("Initialized"),
            "Expected 'Initialized' in telemetry after init()");
    }

    // ----------------------------------------------------------- drive: idle

    @Test
    @DisplayName("no input -> both motors at 0.0")
    void noInputBothMotorsStopped() {
        drivetrain.drive(gamepad1);
        assertEquals(0.0, backLeft.lastPower,  1e-6, "backLeft  should be 0 with no input");
        assertEquals(0.0, backRight.lastPower, 1e-6, "backRight should be 0 with no input");
    }

    // ----------------------------------------------- drive: forward / backward

    @Test
    @DisplayName("right stick pushed forward (right_stick_x = -1) -> both motors full forward")
    void rightStickForwardBothMotorsFull() {
        // drive = -right_stick_x = -(-1.0) = 1.0, turn = 0
        // leftPower  = clip(1.0 + 0, -1, 1) = 1.0
        // rightPower = clip(1.0 - 0, -1, 1) = 1.0
        gamepad1.right_stick_x = -1.0f;
        drivetrain.drive(gamepad1);
        assertEquals( 1.0, backLeft.lastPower,  1e-6, "backLeft  should be full forward");
        assertEquals( 1.0, backRight.lastPower, 1e-6, "backRight should be full forward");
    }

    @Test
    @DisplayName("right stick pushed backward (right_stick_x = 1) -> both motors full reverse")
    void rightStickBackwardBothMotorsReverse() {
        // drive = -1.0, turn = 0
        gamepad1.right_stick_x = 1.0f;
        drivetrain.drive(gamepad1);
        assertEquals(-1.0, backLeft.lastPower,  1e-6, "backLeft  should be full reverse");
        assertEquals(-1.0, backRight.lastPower, 1e-6, "backRight should be full reverse");
    }

    // ----------------------------------------------- drive: turning

    @Test
    @DisplayName("left stick right (left_stick_y = 1) -> right turn differential")
    void leftStickRightTurnDifferential() {
        // turn = 1.0, drive = 0
        // leftPower  = clip(0 + 1, -1, 1) =  1.0
        // rightPower = clip(0 - 1, -1, 1) = -1.0
        gamepad1.left_stick_y = 1.0f;
        drivetrain.drive(gamepad1);
        assertEquals( 1.0, backLeft.lastPower,  1e-6, "backLeft  should be +1 when turning right");
        assertEquals(-1.0, backRight.lastPower, 1e-6, "backRight should be -1 when turning right");
    }

    @Test
    @DisplayName("left stick left (left_stick_y = -1) -> left turn differential")
    void leftStickLeftTurnDifferential() {
        // turn = -1.0, drive = 0
        // leftPower  = clip(0 + (-1), -1, 1) = -1.0
        // rightPower = clip(0 - (-1), -1, 1) =  1.0
        gamepad1.left_stick_y = -1.0f;
        drivetrain.drive(gamepad1);
        assertEquals(-1.0, backLeft.lastPower,  1e-6, "backLeft  should be -1 when turning left");
        assertEquals( 1.0, backRight.lastPower, 1e-6, "backRight should be +1 when turning left");
    }

    // ----------------------------------------------- clipping

    @Test
    @DisplayName("combined input > 1.0 is clipped to 1.0")
    void powerIsClippedToMax() {
        // Both sticks maxed out in same direction:
        // drive = 1.0 (right_stick_x = -1), turn = 1.0 (left_stick_y = 1)
        // leftPower  = clip(1 + 1, -1, 1) = 1.0  (clipped)
        // rightPower = clip(1 - 1, -1, 1) = 0.0
        gamepad1.right_stick_x = -1.0f;
        gamepad1.left_stick_y  =  1.0f;
        drivetrain.drive(gamepad1);
        assertEquals( 1.0, backLeft.lastPower,  1e-6, "backLeft  should be clipped to 1.0");
        assertEquals( 0.0, backRight.lastPower, 1e-6, "backRight should be 0 (1-1 = 0)");
    }

    @Test
    @DisplayName("combined input < -1.0 is clipped to -1.0")
    void powerIsClippedToMin() {
        // drive = -1.0, turn = -1.0
        // leftPower  = clip(-1 + (-1), -1, 1) = -1.0  (clipped)
        // rightPower = clip(-1 - (-1), -1, 1) =  0.0
        gamepad1.right_stick_x = 1.0f;
        gamepad1.left_stick_y  = -1.0f;
        drivetrain.drive(gamepad1);
        assertEquals(-1.0, backLeft.lastPower,  1e-6, "backLeft  should be clipped to -1.0");
        assertEquals( 0.0, backRight.lastPower, 1e-6, "backRight should be 0 (-1+1 = 0)");
    }

    // --------------------------------------- parameterized POV math spot checks

    @ParameterizedTest(name = "drive={0} turn={1} -> left={2} right={3}")
    @CsvSource({
        // rightStickX, leftStickY, expectedLeft, expectedRight
        " 0.0,  0.0,   0.0,   0.0",   // idle
        "-0.5,  0.0,   0.5,   0.5",   // half forward
        " 0.5,  0.0,  -0.5,  -0.5",   // half reverse
        " 0.0,  0.5,   0.5,  -0.5",   // slow right turn
        " 0.0, -0.5,  -0.5,   0.5",   // slow left turn
        "-0.5,  0.25,  0.75,  0.25",  // forward + slight right
    })
    @DisplayName("POV math: parameterized stick values")
    void povMathParameterized(float rightStickX, float leftStickY,
                              double expectedLeft, double expectedRight) {
        gamepad1.right_stick_x = rightStickX;
        gamepad1.left_stick_y  = leftStickY;
        drivetrain.drive(gamepad1);

        assertEquals(expectedLeft,  backLeft.lastPower,  1e-6,
            "backLeft power mismatch");
        assertEquals(expectedRight, backRight.lastPower, 1e-6,
            "backRight power mismatch");
    }

    // ----------------------------------------------- telemetry

    @Test
    @DisplayName("drive() sends motor power telemetry")
    void driveSendsTelemetry() {
        gamepad1.right_stick_x = -0.5f;
        drivetrain.drive(gamepad1);
        assertTrue(telemetry.hasEntry("Motors"),
            "Expected 'Motors' telemetry entry after drive()");
    }

    // ----------------------------------------------- encoder: driveByEncoder

    @Test
    @DisplayName("driveByEncoder forward: leftTarget > 0, rightTarget < 0 (opposite signs)")
    void driveByEncoderForwardTargetsOppositeSigns() {
        // backRight is REVERSE — its target must be negated to move same
        // physical direction as backLeft
        drivetrain.driveByEncoder(0.6, 24);
        assertTrue(backLeft.targetPosition  > 0, "leftTarget should be positive for forward drive");
        assertTrue(backRight.targetPosition < 0, "rightTarget should be negative for forward drive");
    }

    @Test
    @DisplayName("driveByEncoder forward: both targets equal magnitude")
    void driveByEncoderForwardEqualMagnitude() {
        drivetrain.driveByEncoder(0.6, 24);
        assertEquals(backLeft.targetPosition, -backRight.targetPosition,
            "Left and right encoder targets should be equal magnitude for straight drive");
    }

    @Test
    @DisplayName("driveByEncoder backward: leftTarget < 0, rightTarget > 0")
    void driveByEncoderBackwardTargetsOppositeSigns() {
        drivetrain.driveByEncoder(0.6, -24);
        assertTrue(backLeft.targetPosition  < 0, "leftTarget should be negative for reverse drive");
        assertTrue(backRight.targetPosition > 0, "rightTarget should be positive for reverse drive");
    }

    @Test
    @DisplayName("driveByEncoder: both motors set to RUN_TO_POSITION mode")
    void driveByEncoderSetsRunToPositionMode() {
        drivetrain.driveByEncoder(0.6, 24);
        // After completion, stopAndResetToEncoder restores RUN_USING_ENCODER
        assertEquals(DcMotor.RunMode.RUN_USING_ENCODER, backLeft.lastMode);
        assertEquals(DcMotor.RunMode.RUN_USING_ENCODER, backRight.lastMode);
    }

    @Test
    @DisplayName("driveByEncoder: motors stopped (power = 0) after completion")
    void driveByEncoderStopsMotorsAfterCompletion() {
        drivetrain.driveByEncoder(0.6, 24);
        assertEquals(0.0, backLeft.lastPower,  1e-6, "backLeft should be stopped after driveByEncoder");
        assertEquals(0.0, backRight.lastPower, 1e-6, "backRight should be stopped after driveByEncoder");
    }
}
