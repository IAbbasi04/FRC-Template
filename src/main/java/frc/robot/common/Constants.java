package frc.robot.common;

import edu.wpi.first.math.util.Units;

public class Constants {
    public static class FIELD {
        public static final double RED_WALL_X = 16.542;
    }

    public final class LOGGING {
        public static final String LOG_FOLDER = "CustomLogs";
    }

    public static class POWER {
        public static final String LOG_PATH = LOGGING.LOG_FOLDER + "/Power/";

        public static final int SWERVE_MAX_VOLTAGE = 12;
        public static final int SWERVE_TELEOP_THROTTLE_CURRENT_LIMIT = 80;
        public static final int SWERVE_AUTO_THROTTLE_CURRENT_LIMIT = 60;
        public static final int SWERVE_AZIMUTH_CURRENT_LIMIT = 40;

        public static final int VOLTAGE_SMOOTHING_LENGTH = 50; // TODO: Arbitrary (needs testing)
        public static final double DISABLED_LOW_BATTERY_VOLTAGE = 11.5; // TODO: Arbitrary (needs testing)
        public static final double TELEOP_LOW_BATTERY_VOLTAGE = 10.5; // TODO: Arbitrary (needs testing)
    }

    public static class INPUT {
        public static final double PRESSING_AXIS_DEADBAND = 0.25;

        public static final int DRIVER_CONTROLLER_PORT = 0;
        public static final int OPERATOR_CONTROLLER_PORT = 1;

        public static final String DRIVER_SHUFFLEBOARD_TAB = "Driver";
        public static final String MANIPULATOR_SHUFFLEBOARD_TAB = "Manipulator";
    }

    public static class SWERVE {
        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(145.02+180); // Black Module
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(339.785-180); // Orange Module
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(62.93+180); // Teal Module
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(3.867+180); // White Module

        public static final double THROTTLE_kP = 0.02;
        public static final double THROTTLE_kI = 0.0;
        public static final double THROTTLE_kD = 0.01;

        public static final double STEER_kP = 0.2;
        public static final double STEER_kI = 0.0;
        public static final double STEER_kD = 0.1;

        public static final double DRIVE_TRAIN_WIDTH = Units.inchesToMeters(32.875); // meters
        public static final double DRIVE_TRAIN_LENGTH = Units.inchesToMeters(35.750); // meters
        public static final double WHEEL_CIRCUMFERENCE = 4 * Math.PI; // inches

        public static final double DRIVE_TRAIN_RADIUS = Math.sqrt(Math.pow(DRIVE_TRAIN_WIDTH, 2) + Math.pow(DRIVE_TRAIN_LENGTH, 2));

        public static final double TRANSLATE_POWER_FAST = 1.0; // Scaling for teleop driving.  1.0 is maximum
        public static final double ROTATE_POWER_FAST = 0.75; // Scaling for teleop driving.  1.0 is maximum
        public static final double TRANSLATE_POWER_SLOW = 0.15; // Scaling for teleop driving.  1.0 is maximum
        public static final double ROTATE_POWER_SLOW = 0.15; // Scaling for teleop driving.  1.0 is maximum   

        public static final double MAX_VOLTAGE = 12.0;
        public static final double MAX_VELOCITY_METERS_PER_SECOND = 4.5;
        public static final double MAX_ACCELERATION_METERS_PER_SECOND_PER_SECOND = 4.5;

        public static final double SLEW_LIMIT = 2.0; // 200% rate of change per second
    }

    public static class INTAKE {

    }

    public static class VISION {
        
    }
}
