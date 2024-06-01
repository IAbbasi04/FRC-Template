package frc.robot.controls;

import lib.frc8592.controls.xbox.XboxInput;

public class InputMap {
    public static class DRIVER extends InputMap {
        public static XboxInput
            TRANSLATE_Y = XboxInput.LEFT_X_AXIS,
            TRANSLATE_X = XboxInput.LEFT_Y_AXIS,
            ROTATE = XboxInput.RIGHT_X_AXIS,
            SNAIL_MODE = XboxInput.RIGHT_BUMPER,
            RESET_GYRO = XboxInput.BACK_BTN,
            
            SPEAKER_TARGET_LOCK = XboxInput.RIGHT_TRIGGER_AXIS,
            NOTE_TARGET_LOCK = XboxInput.LEFT_TRIGGER_AXIS
            ;
    }

    public static class MANIPULATOR extends InputMap {
        public static XboxInput
            INTAKE = XboxInput.LEFT_TRIGGER_AXIS,
            OUTAKE = XboxInput.LEFT_BUMPER,
            SCORE = XboxInput.RIGHT_TRIGGER_AXIS,

            STOW = XboxInput.A_BTN,
            PRIME = XboxInput.B_BTN,
            AMP_POSITION = XboxInput.X_BTN,
            CLIMB_POSITION = XboxInput.Y_BTN,

            EXTENSION_RAISE = XboxInput.DPAD_UP,
            EXTENSION_LOWER = XboxInput.DPAD_DOWN,

            MANUAL_MODE = XboxInput.START,

            MANUAL_SUBWOOFER_SHOT = XboxInput.LEFT_BUMPER,
            MANUAL_PODIUM_SHOT = XboxInput.RIGHT_BUMPER
            ;
    }
}
