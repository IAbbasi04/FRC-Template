package frc.robot.common;

import edu.wpi.first.math.util.Units;

public class Constants {
    public static class FIELD {
        public static final double RED_WALL_X = 16.542;
    }

    public static class INPUT {
        public static final double PRESSING_AXIS_DEADBAND = 0.25;
        public static final int DRIVER_CONTROLLER_PORT = 0;
        public static final int OPERATOR_CONTROLLER_PORT = 1;
    }

    public static class SWERVE {
        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(233.525); // Teal Module
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(133.77); // Orange Module
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(78.75); // Black Module
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(147.041); // White Module

        public static final double THROTTLE_kP = 0.02;
        public static final double THROTTLE_kI = 0.0;
        public static final double THROTTLE_kD = 0.01;

        public static final double STEER_kP = 0.2;
        public static final double STEER_kI = 0.0;
        public static final double STEER_kD = 0.1;

        public static final double DRIVE_TRAIN_WIDTH = Units.inchesToMeters(32.875); // meters
        public static final double DRIVE_TRAIN_LENGTH = Units.inchesToMeters(35.750); // meters
        public static final double WHEEL_CIRCUMFERENCE = 4 * Math.PI; // inches

        public static final double TRANSLATE_POWER_FAST = 1.0; // Scaling for teleop driving.  1.0 is maximum
        public static final double ROTATE_POWER_FAST = 0.75; // Scaling for teleop driving.  1.0 is maximum
        public static final double TRANSLATE_POWER_SLOW = 0.15; // Scaling for teleop driving.  1.0 is maximum
        public static final double ROTATE_POWER_SLOW = 0.15; // Scaling for teleop driving.  1.0 is maximum   

        public static final double MAX_VOLTAGE = 12.0;
        public static final double MAX_VELOCITY_METERS_PER_SECOND = 4.5;
        public static final double MAX_ACCELERATION_METERS_PER_SECOND_PER_SECOND = 4.5;

        public static final double SLEW_LIMIT = 2.0; // 200% rate of change per second
    }
}
