package frc.robot.controls;

public class InputMap {
    public static class DRIVER extends InputMap {
        public static EXboxController
            TRANSLATE_Y = EXboxController.LEFT_X_AXIS,
            TRANSLATE_X = EXboxController.LEFT_Y_AXIS,
            ROTATE = EXboxController.RIGHT_X_AXIS,
            SNAIL_MODE = EXboxController.RIGHT_BUMPER,
            RESET_GYRO = EXboxController.BACK_BTN,
            
            SPEAKER_TARGET_LOCK = EXboxController.LEFT_TRIGGER_AXIS
            ;
    }

    public static class MANIPULATOR extends InputMap {
        public static EXboxController
            INTAKE = EXboxController.LEFT_TRIGGER_AXIS,
            OUTAKE = EXboxController.LEFT_BUMPER,
            SCORE_RANGED = EXboxController.RIGHT_TRIGGER_AXIS,
            MANUAL_MODE = EXboxController.START;
    }
}
