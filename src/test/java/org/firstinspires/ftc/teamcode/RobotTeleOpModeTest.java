package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.mocks.MockCRServo;
import org.firstinspires.ftc.teamcode.mocks.MockDcMotor;
import org.firstinspires.ftc.teamcode.mocks.MockTelemetry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-level test for RobotTeleOpMode_Linear.
 *
 * Because opModeIsActive() is overridden to run exactly N iterations, we
 * can simulate a short match segment without any real hardware.
 */
@DisplayName("RobotTeleOpMode_Linear integration")
class RobotTeleOpModeTest {

    /**
     * Test-friendly subclass of RobotTeleOpMode_Linear.
     * We control exactly how many loop iterations execute and which
     * telemetry / hardwareMap instances are used.
     */
    static class TestableOpMode extends RobotTeleOpMode_Linear {
        private int remainingIterations;

        TestableOpMode(MockTelemetry tel, HardwareMap hwMap, int iterations) {
            this.telemetry    = tel;
            this.hardwareMap  = hwMap;
            remainingIterations = iterations;
        }

        @Override
        public boolean opModeIsActive() {
            if (remainingIterations > 0) {
                remainingIterations--;
                return true;
            }
            return false;
        }
    }

    private HardwareMap buildHardwareMap(MockDcMotor backLeft, MockDcMotor backRight,
                                          MockCRServo ballPusher) {
        HardwareMap hwMap = new HardwareMap();
        hwMap.put(DcMotor.class, "backLeft",   backLeft);
        hwMap.put(DcMotor.class, "backRight",  backRight);
        hwMap.put(DcMotor.class, "flyWheel",   new MockDcMotor());
        hwMap.put(DcMotor.class, "intake",     new MockDcMotor());
        hwMap.put(CRServo.class, "ballPusher", ballPusher);
        return hwMap;
    }

    // ------------------------------------------------------------------ init

    @Test
    @DisplayName("runOpMode() initialises all hardware without errors")
    void runOpModeInitializesSuccessfully() {
        MockDcMotor backLeft  = new MockDcMotor();
        MockDcMotor backRight = new MockDcMotor();
        MockCRServo ballPusher = new MockCRServo();
        MockTelemetry telemetry = new MockTelemetry();

        TestableOpMode opMode = new TestableOpMode(telemetry,
                buildHardwareMap(backLeft, backRight, ballPusher), 0);

        assertDoesNotThrow(opMode::runOpMode,
            "runOpMode() should complete without exceptions even with mock hardware");
    }

    @Test
    @DisplayName("runOpMode() sends Initialized telemetry")
    void runOpModeSendsInitializedTelemetry() {
        MockTelemetry telemetry = new MockTelemetry();
        TestableOpMode opMode = new TestableOpMode(telemetry,
                buildHardwareMap(new MockDcMotor(), new MockDcMotor(), new MockCRServo()), 0);

        opMode.runOpMode();

        assertTrue(telemetry.hasEntry("Initialized"),
            "Expected 'Initialized' telemetry during runOpMode()");
    }

    // --------------------------------------------------------------- one loop iteration

    @Test
    @DisplayName("one iteration with A button: ballPusher runs in reverse")
    void oneIterationButtonAActivatesServo() {
        MockCRServo ballPusher = new MockCRServo();
        MockTelemetry telemetry = new MockTelemetry();
        TestableOpMode opMode = new TestableOpMode(telemetry,
                buildHardwareMap(new MockDcMotor(), new MockDcMotor(), ballPusher), 1);

        opMode.gamepad2.a = true;
        opMode.runOpMode();

        assertEquals(-1.0, ballPusher.lastPower, 1e-6,
            "Pressing gamepad2.A for one iteration should set ballPusher to -1.0");
    }

    @Test
    @DisplayName("one iteration driving forward: both drive motors get positive power")
    void oneIterationDriveForward() {
        MockDcMotor backLeft  = new MockDcMotor();
        MockDcMotor backRight = new MockDcMotor();
        MockTelemetry telemetry = new MockTelemetry();
        TestableOpMode opMode = new TestableOpMode(telemetry,
                buildHardwareMap(backLeft, backRight, new MockCRServo()), 1);

        // Push right stick forward on gamepad1
        opMode.gamepad1.right_stick_x = -1.0f;
        opMode.runOpMode();

        assertEquals(1.0, backLeft.lastPower,  1e-6, "backLeft should be 1.0 driving forward");
        assertEquals(1.0, backRight.lastPower, 1e-6, "backRight should be 1.0 driving forward");
    }

    // --------------------------------------------------------------- multiple iterations

    @Test
    @DisplayName("multiple iterations: last gamepad state is reflected in motor powers")
    void multipleIterationsLastStateWins() {
        MockDcMotor backLeft  = new MockDcMotor();
        MockDcMotor backRight = new MockDcMotor();
        MockTelemetry telemetry = new MockTelemetry();
        TestableOpMode opMode = new TestableOpMode(telemetry,
                buildHardwareMap(backLeft, backRight, new MockCRServo()), 5);

        // Change the gamepad state mid-run — the last iteration sets right_stick_x = 0
        // so both motors end at 0.0
        opMode.gamepad1.right_stick_x = -1.0f; // forward for all iterations
        opMode.runOpMode();

        // After 5 iterations still driving forward
        assertEquals(1.0, backLeft.lastPower,  1e-6);
        assertEquals(1.0, backRight.lastPower, 1e-6);
    }
}
