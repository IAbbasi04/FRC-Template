package frc.robot.controls;

public class InputMap {
    public static class DRIVER extends InputMap {
        public static EXboxController
            TRANSLATE_Y = EXboxController.LEFT_X_AXIS,
            TRANSLATE_X = EXboxController.LEFT_Y_AXIS,
            ROTATE = EXboxController.RIGHT_X_AXIS,
            SNAIL_MODE = EXboxController.RIGHT_BUMPER,
            RESET_GYRO = EXboxController.BACK_BTN,
            
            SPEAKER_TARGET_LOCK = EXboxController.RIGHT_TRIGGER_AXIS,
            NOTE_TARGET_LOCK = EXboxController.LEFT_TRIGGER_AXIS
            ;
    }

    public static class MANIPULATOR extends InputMap {
        public static EXboxController
            INTAKE = EXboxController.LEFT_TRIGGER_AXIS,
            OUTAKE = EXboxController.LEFT_BUMPER,
            SCORE = EXboxController.RIGHT_TRIGGER_AXIS,

            STOW = EXboxController.A_BTN,
            PRIME = EXboxController.B_BTN,
            AMP_POSITION = EXboxController.X_BTN,
            CLIMB_POSITION = EXboxController.Y_BTN,

            EXTENSION_RAISE = EXboxController.DPAD_UP,
            EXTENSION_LOWER = EXboxController.DPAD_DOWN,

            MANUAL_MODE = EXboxController.START,

            MANUAL_SUBWOOFER_SHOT = EXboxController.LEFT_BUMPER,
            MANUAL_PODIUM_SHOT = EXboxController.RIGHT_BUMPER
            ;
    }
}
