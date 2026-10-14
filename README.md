# BioBuzzFTC — Team 24632

FTC TeleOp robot code with a local testing setup that lets you validate logic
on any Windows PC without a robot, a phone, or the Android SDK.

---

## Project structure

```
BioBuzzFTC/
│
├── com/firstinspires/ftc/teamcode/   ← YOUR TEAM CODE — edit here, upload these to robot
│   ├── BallPusherServo.java
│   ├── BasicTwoWheelBackDriveTrain.java
│   ├── RobotTeleOpMode_Linear.java
│   └── ... add new files here
│
├── src/
│   ├── main/java/                    ← FTC SDK stubs (PC testing only, do not edit)
│   │   ├── com/qualcomm/robotcore/
│   │   │   ├── eventloop/opmode/
│   │   │   │   ├── Disabled.java
│   │   │   │   ├── LinearOpMode.java
│   │   │   │   └── TeleOp.java
│   │   │   ├── hardware/
│   │   │   │   ├── CRServo.java
│   │   │   │   ├── DcMotor.java
│   │   │   │   ├── DcMotorSimple.java
│   │   │   │   ├── Gamepad.java
│   │   │   │   └── HardwareMap.java
│   │   │   └── util/
│   │   │       ├── ElapsedTime.java
│   │   │       └── Range.java
│   │   └── org/firstinspires/ftc/robotcore/external/
│   │       └── Telemetry.java
│   │
│   └── test/java/org/firstinspires/ftc/teamcode/
│       ├── mocks/
│       │   ├── MockCRServo.java         ← records servo power for assertions
│       │   ├── MockDcMotor.java         ← records motor power & direction
│       │   └── MockTelemetry.java       ← captures addData() calls
│       ├── BallPusherServoTest.java      ← 8 tests
│       ├── BasicTwoWheelBackDriveTrainTest.java  ← 14 tests
│       └── RobotTeleOpModeTest.java     ← 5 integration tests
│
├── gradle/wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties        ← Gradle 8.14.3 (Java 24 compatible)
├── gradlew.bat                          ← run tests on Windows
├── build.gradle
└── settings.gradle
```

Gradle compiles `com/firstinspires/ftc/teamcode/` and `src/main/java/` together,
so your team code and the SDK stubs are always in sync. There is only one copy of
your team code — no manual file-copying needed.

---

## Testing without the robot

### Prerequisites

| Requirement | Version |
|---|---|
| Java JDK | 17 – 24 (Java 24 confirmed working) |
| Internet access | needed on first run to download Gradle |

No Android SDK, no FTC Robot Controller app, no phone, no hardware needed.

### How it works

The `src/main/java` tree contains **stub implementations** of every FTC SDK
class your team code imports (`HardwareMap`, `DcMotor`, `CRServo`, `Gamepad`,
`LinearOpMode`, etc.).  These stubs compile and run as plain Java on any PC.

The test suite uses **mock objects** (`MockDcMotor`, `MockCRServo`,
`MockTelemetry`) that record what your code does — which power values it sets,
which telemetry it sends — so JUnit assertions can check correctness without
touching real hardware.

### Run the tests

Open a PowerShell terminal in the project root and run:

```powershell
.\gradlew.bat test
```

First run downloads Gradle (~150 MB) and JUnit dependencies automatically.
Subsequent runs are fast because Gradle caches everything.

To force tests to re-execute even when no source changed:

```powershell
.\gradlew.bat test --rerun-tasks
```

### Reading the results

Results are printed to the terminal as each test runs:

```
BallPusherServo > gamepad2.a pressed -> servo power = -1.0  PASSED
BasicTwoWheelBackDriveTrain > no input -> both motors at 0.0  PASSED
...
BUILD SUCCESSFUL in 18s
```

An HTML report with full detail is generated after every run at:

```
build/reports/tests/test/index.html
```

Open that file in any browser for a formatted summary with pass/fail counts.

### What is currently tested

#### `BallPusherServoTest` (8 tests)

| Test | What it verifies |
|---|---|
| `init()` sends Initialized telemetry | Telemetry fires on startup |
| `init()` calls `telemetry.update()` | Driver Station display updates |
| `gamepad2.a` pressed → power = -1.0 | A button activates the servo |
| `gamepad2.a` released → power = 0.0 | Releasing A stops the servo |
| No button pressed → power = 0.0 | Default state is stopped |
| `gamepad2.b` pressed → power = 0.0 | Unmapped buttons have no effect |
| `spinBallPusher()` sends Running telemetry | Status telemetry fires each loop |
| Toggle press/release cycles correctly | Repeated state changes work |

#### `BasicTwoWheelBackDriveTrainTest` (14 tests)

| Test | What it verifies |
|---|---|
| `init()` sets `backLeft` → FORWARD | Motor direction wired correctly |
| `init()` sets `backRight` → REVERSE | Opposite side reversed as expected |
| `init()` sends Initialized telemetry | Startup telemetry works |
| No input → both motors at 0.0 | Idle state is safe |
| Right stick forward → both motors 1.0 | Full-speed forward drive |
| Right stick backward → both motors -1.0 | Full-speed reverse drive |
| Left stick right → left +1.0, right -1.0 | Point-turn right |
| Left stick left → left -1.0, right +1.0 | Point-turn left |
| Combined input > 1.0 is clipped to 1.0 | `Range.clip()` prevents over-power |
| Combined input < -1.0 is clipped to -1.0 | Reverse also clipped |
| `drive()` sends motor power telemetry | Loop telemetry works |
| 6× parameterized POV math checks | Intermediate stick values are correct |

#### `RobotTeleOpModeTest` (5 integration tests)

| Test | What it verifies |
|---|---|
| `runOpMode()` with 0 iterations completes without error | Init path is clean |
| `runOpMode()` sends Initialized telemetry | Top-level telemetry fires |
| 1 iteration, gamepad2.a = true → servo -1.0 | Subsystems are wired together |
| 1 iteration, right stick forward → motors 1.0 | Drivetrain subsystem fires |
| 5 iterations, stick held → final motor state correct | Loop runs consistently |

### Adding new tests

1. **Write your new class** in `com/firstinspires/ftc/teamcode/` — that's it,
   no copying needed. Gradle picks it up automatically.
2. If it uses a new hardware type (e.g., a color sensor), add a stub interface
   under `src/main/java/com/qualcomm/robotcore/hardware/` and a matching mock
   under `src/test/java/.../mocks/`.
3. Add a new test class under `src/test/java/org/firstinspires/ftc/teamcode/`
   following the same `@BeforeEach` / `@Test` pattern as the existing tests.
4. Run `.\gradlew.bat test`.

---

## Deploying to the robot

Only your **team code files** go to the robot. The SDK stubs, Gradle files,
and test code stay on your PC — the real robot already has the FTC SDK built
into the Robot Controller app.

### Files to upload

These are the three files that belong on the Robot Controller:

```
com/firstinspires/ftc/teamcode/BallPusherServo.java
com/firstinspires/ftc/teamcode/BasicTwoWheelBackDriveTrain.java
com/firstinspires/ftc/teamcode/RobotTeleOpMode_Linear.java
```

As you add more classes, every new `.java` file in that folder also belongs
on the robot. The rule is simple: **everything in `com/` goes to the robot,
nothing else does.**

### How to upload using OnBot Java

1. Connect your laptop to the Robot Controller Wi-Fi network.
2. Open a browser and go to `http://192.168.43.1:8080` (the OnBot Java editor).
3. For each of the three files above, either:
   - Paste the file contents into the OnBot editor, or
   - Use the upload button (⬆) in OnBot to upload the `.java` file directly.
4. Click **Build Everything** (the wrench icon).
5. If the build log shows `BUILD SUCCESSFUL`, the OpMode is ready.
6. On the Driver Station, select **RobotTeleOpMode_Linear** from the TeleOp menu.

### Robot configuration (hardware names)

The code looks up hardware by name from the Robot Controller configuration.
These names must match exactly — they are case-sensitive:

| Config name | Type | Used in |
|---|---|---|
| `backLeft` | DC Motor | `BasicTwoWheelBackDriveTrain` |
| `backRight` | DC Motor | `BasicTwoWheelBackDriveTrain` |
| `ballPusher` | CR Servo | `BallPusherServo` |

If a name doesn't match, the OpMode will crash on init with a
`"Could not find..."` error on the Driver Station.

### Files that must NOT go to the robot

These are PC-only and would either conflict with the real SDK or have no
purpose on the robot:

```
src/main/java/com/qualcomm/robotcore/**   ← SDK stubs
src/main/java/org/firstinspires/ftc/robotcore/**  ← Telemetry stub
src/test/**                               ← test code and mocks
build.gradle
settings.gradle
gradlew.bat
gradle/
```

---

## Recommended workflow

```
Write / edit files in com/firstinspires/ftc/teamcode/
        │
        │  (no copying needed — Gradle reads this folder directly)
        │
        ▼
Run .\gradlew.bat test   ──── all tests pass?
        │ yes
        ▼
Upload everything in com/firstinspires/ftc/teamcode/ to OnBot Java
        │
        ▼
Build on robot → physical test
```

---

## Gamepad layout reference

| Gamepad | Stick / Button | Action |
|---|---|---|
| gamepad1 (driver) | Right stick X | Forward / reverse |
| gamepad1 (driver) | Left stick Y | Turn left / right |
| gamepad2 (operator) | A button | Run ball pusher (reverse, -1.0 power) |
| gamepad2 (operator) | A released | Stop ball pusher |
