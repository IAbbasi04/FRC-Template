package lib.frc8592.controls.xbox;
import lib.frc8592.controls.*;

import edu.wpi.first.wpilibj.DriverStation;

public class XboxController extends Controller<XboxInput> {
    public XboxController(int port) {
        super(port);
    }

    @Override
    public double get(XboxInput input) {
        return input.get(joystick);
    }

    @Override
    public boolean isPressing(XboxInput input) {
        return Math.abs(input.get(joystick)) >= 0.25;
    }

    @Override
    public boolean getPressed(XboxInput input) {
        return DriverStation.getStickButtonPressed(joystick.getPort(), input.getButtonID());
    }

    @Override
    public boolean getReleased(XboxInput input) {
        return DriverStation.getStickButtonReleased(joystick.getPort(), input.getButtonID());
    }

    /**
     * Shakes the controller at the desired intensity [0, 1]
     */
    public void setRumble(double intensity) {
        joystick.setRumble(RumbleType.kBothRumble, intensity);
    }
}