package com.qualcomm.robotcore.hardware;

/**
 * Stub for FTC Gamepad.
 *
 * All fields are public, matching the real SDK.  Set them directly in tests:
 *   gamepad.left_stick_y = 0.5f;
 *   gamepad.a = true;
 */
public class Gamepad {
    // Left joystick
    public float left_stick_x  = 0.0f;
    public float left_stick_y  = 0.0f;

    // Right joystick
    public float right_stick_x = 0.0f;
    public float right_stick_y = 0.0f;

    // Face buttons
    public boolean a = false;
    public boolean b = false;
    public boolean x = false;
    public boolean y = false;

    // Bumpers / triggers
    public boolean left_bumper  = false;
    public boolean right_bumper = false;
    public float   left_trigger  = 0.0f;
    public float   right_trigger = 0.0f;

    // D-pad
    public boolean dpad_up    = false;
    public boolean dpad_down  = false;
    public boolean dpad_left  = false;
    public boolean dpad_right = false;

    // Stick buttons
    public boolean left_stick_button  = false;
    public boolean right_stick_button = false;

    // Start / back / guide
    public boolean start = false;
    public boolean back  = false;
    public boolean guide = false;
}
