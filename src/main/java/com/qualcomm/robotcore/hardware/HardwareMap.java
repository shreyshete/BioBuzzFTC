package com.qualcomm.robotcore.hardware;

import java.util.HashMap;
import java.util.Map;

/**
 * Stub for FTC HardwareMap.
 *
 * Register mock hardware objects before calling init():
 *   hardwareMap.put(DcMotor.class, "backLeft", myMockMotor);
 *
 * Your team code then retrieves them via:
 *   hardwareMap.get(DcMotor.class, "backLeft");
 */
public class HardwareMap {

    // Nested key: type + name -> hardware object
    private final Map<String, Object> devices = new HashMap<>();

    private static String key(Class<?> type, String name) {
        return type.getName() + ":" + name;
    }

    /** Register a mock/stub device so get() can return it. */
    public <T> void put(Class<T> type, String name, T device) {
        devices.put(key(type, name), device);
    }

    /**
     * Retrieve a registered hardware device, exactly as the real SDK works.
     * Throws IllegalArgumentException if no device was registered for that type+name.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type, String name) {
        Object device = devices.get(key(type, name));
        if (device == null) {
            throw new IllegalArgumentException(
                "No mock registered for " + type.getSimpleName() + " named \"" + name + "\". " +
                "Call hardwareMap.put(" + type.getSimpleName() + ".class, \"" + name + "\", mock) in your test setup."
            );
        }
        return type.cast(device);
    }
}
