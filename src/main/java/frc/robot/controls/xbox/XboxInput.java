package frc.robot.controls.xbox;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.controls.ControllerInput;

public enum XboxInput implements ControllerInput{
    // Do not change the order of these buttons
    A_BTN,
    B_BTN,
	X_BTN,
	Y_BTN,
	LEFT_BUMPER,
	RIGHT_BUMPER,
    BACK_BTN,
    START,
    LEFT_JOYSTICK_BTN,
	RIGHT_JOYSTICK_BTN,
    LEFT_TRIGGER_BTN,
    RIGHT_TRIGGER_BTN,
	DPAD_RAW,
	DPAD_UP,
	DPAD_DOWN,
	DPAD_LEFT,
	DPAD_RIGHT,
	LEFT_X_AXIS,
	LEFT_Y_AXIS,
    LEFT_TRIGGER_AXIS,
    RIGHT_TRIGGER_AXIS,
    RIGHT_X_AXIS,
    RIGHT_Y_AXIS,
    NULL;

    public double get(Joystick controller) {
        double dpad = controller.getPOV();
        boolean LTPressed = Math.abs(controller.getRawAxis(LEFT_TRIGGER_AXIS.ordinal() - 17)) >= 0.2;
        boolean RTPressed = Math.abs(controller.getRawAxis(RIGHT_TRIGGER_AXIS.ordinal() - 17)) >= 0.2;

        switch(this) {
            case DPAD_RAW: // DPAD Values
                return dpad;
            case DPAD_UP:
                return (dpad > 315 || (dpad < 45 && dpad >= 0)) ? 1 : 0;
            case DPAD_RIGHT:
                return (dpad > 45 && dpad < 135) ? 1 : 0;
            case DPAD_DOWN:
                return (dpad > 135 && dpad < 225) ? 1 : 0;
            case DPAD_LEFT:
                return (dpad > 225 && dpad < 315) ? 1 : 0;
            case X_BTN: // Button Values
            case A_BTN:
            case B_BTN:
            case Y_BTN:
            case LEFT_BUMPER:
            case RIGHT_BUMPER:
            case BACK_BTN:
            case START:
            case LEFT_JOYSTICK_BTN:
            case RIGHT_JOYSTICK_BTN:
                return controller.getRawButton(ordinal() + 1) ? 1 : 0;
            case LEFT_X_AXIS: // Axis Values
            case LEFT_Y_AXIS:
            case RIGHT_X_AXIS:
            case RIGHT_Y_AXIS:
            case LEFT_TRIGGER_AXIS:
            case RIGHT_TRIGGER_AXIS:
                double axisValue = controller.getRawAxis(this.ordinal() - 17);
                return Math.abs(axisValue) >= 0.02 ? axisValue : 0.0;
            case LEFT_TRIGGER_BTN:
                return LTPressed ? 1.0 : 0.0;
            case RIGHT_TRIGGER_BTN:
                return RTPressed ? 1.0 : 0.0;
            default:
                return 0.0;
        }
    }

    public int getButtonID() {
        return ordinal() + 1;
    }

    /**
     * Returns all inputs in a way that can be sent to shuffleboard
     */
    public static SendableChooser<XboxInput> getAsSendable(XboxInput defaultOption) {
        SendableChooser<XboxInput> chooser = new SendableChooser<>();
        chooser.setDefaultOption(defaultOption.name(), defaultOption);
        for (XboxInput input : values()) {
            chooser.addOption(input.name(), input);
        }
        return chooser;
    }

    public String toString() {
        return name();
    }
}