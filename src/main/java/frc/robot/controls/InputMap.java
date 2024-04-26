package frc.robot.controls;

import org.frc8592.controls.xbox.XboxInput;

public class InputMap {
    public static class DRIVER extends InputMap {
        public static XboxInput
            TRANSLATE_Y = XboxInput.LEFT_X_AXIS,
            TRANSLATE_X = XboxInput.LEFT_Y_AXIS,
            ROTATE = XboxInput.RIGHT_X_AXIS,
            SNAIL_MODE = XboxInput.RIGHT_BUMPER,
            RESET_GYRO = XboxInput.BACK_BTN
            ;
    }

    public static class MANIPULATOR extends InputMap {
        public static XboxInput
            INTAKE = XboxInput.LEFT_TRIGGER_AXIS
            ;
    }
}
