package lib.frc8592.controls;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Interface useful for allowing easy swapping between different controllers' inputs
 */
public interface ControllerInput {
    /**
     * Gets the value for desired input on a specified controller
     */
    public double get(Joystick controller);

    /**
     * Driver station recognized id of current button
     */
    public int getButtonID();
}