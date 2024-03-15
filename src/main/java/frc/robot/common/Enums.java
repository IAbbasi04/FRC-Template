package frc.robot.common;

public class Enums {
    public static enum MatchMode {
        DISABLED,
        AUTONOMOUS,
        TELEOP,
        TEST;

        public boolean is(MatchMode mode) {
            return this == mode;
        }

        public boolean isAny(MatchMode... modes) {
            for (MatchMode mode : modes) {
                if (this == mode) return true;
            }
            return false;
        }
    }
}