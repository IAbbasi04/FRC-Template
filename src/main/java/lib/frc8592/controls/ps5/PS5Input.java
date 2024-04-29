package lib.frc8592.controls.ps5;

import lib.frc8592.controls.ControllerInput;

import edu.wpi.first.wpilibj.Joystick;

public enum PS5Input implements ControllerInput {
    // Do NOT change order
    SQUARE_BTN,
    CROSS_BTN,
    CIRCLE_BTN,
    TRIANGLE_BTN,
    LEFT_BUMPER,
    RIGHT_BUMPER,
    LEFT_TRIGGER_BTN,
    RIGHT_TRIGGER_BTN,
    MEDIA_BTN,
    START_BTN,
    LEFT_JOYSTICK_BTN,
    RIGHT_JOYSTICK_BTN,
    POWER_BTN,
    MIDDLE_BTN,

    LEFT_X_AXIS,
    LEFT_Y_AXIS,
    RIGHT_X_AXIS,
    LEFT_TRIGGER_AXIS,
    RIGHT_TRIGGER_AXIS,
    RIGHT_Y_AXIS;

    @Override
    public double get(Joystick controller) {
        switch (this) {
            case SQUARE_BTN:
            case CROSS_BTN:
            case CIRCLE_BTN:
            case TRIANGLE_BTN:
            case LEFT_BUMPER:
            case RIGHT_BUMPER:
            case LEFT_TRIGGER_BTN:
            case RIGHT_TRIGGER_BTN:
            case MEDIA_BTN:
            case START_BTN:
            case LEFT_JOYSTICK_BTN:
            case RIGHT_JOYSTICK_BTN:
            case POWER_BTN:
            case MIDDLE_BTN: // Button cases
                return controller.getRawButton(ordinal() + 1) ? 1.0 : 0.0;
            case LEFT_TRIGGER_AXIS:
            case RIGHT_TRIGGER_AXIS: // Trigger cases
                return (controller.getRawAxis(ordinal() - 14) / 2.0) + 0.5;
            default: // Joystick cases
                return controller.getRawAxis(ordinal() - 14);
        }
    }

    @Override
    public int getButtonID() {
        return this.ordinal() + 1;
    }
}
