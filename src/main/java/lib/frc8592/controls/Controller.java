package lib.frc8592.controls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public abstract class Controller<T extends ControllerInput> extends GenericHID {
    protected Joystick joystick;

    public Controller(int port) {
        super(port);
        this.joystick = new Joystick(port);
    }

    /**
     * Gets the [-1, 1] value for an axis input and {0, 1} for buttons; DPAD_RAW returns in degrees [0, 360]
     */
    public abstract double get(T input);

    /**
     * Determines whether a particular input is currently being pressed or has a non-zero value
     */
    public abstract boolean isPressing(T input);

    /**
     * Determines whether any of the indicated inputs are being pressed
     */
    public abstract boolean isPressingAny(T[] inputs);

    /**
     * Determines whether all of the indicated inputs are being pressed
     */
    public abstract boolean isPressingAll(T[] inputs);

    /**
     * Returns whether the input has just been pressed (Only works the frame of pressing); Currently only works on buttons
     */
    public abstract boolean getPressed(T input);

    /**
     * Returns whether the input is no longer being pressed (Only works the frame of releasing); Currently only works on buttons
     */
    public abstract boolean getReleased(T input);
}