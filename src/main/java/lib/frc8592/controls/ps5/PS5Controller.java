package lib.frc8592.controls.ps5;

import lib.frc8592.controls.Controller;
import edu.wpi.first.wpilibj.DriverStation;

public class PS5Controller extends Controller<PS5Input> {
    public PS5Controller(int port) {
        super(port);
    }

    @Override
    public double get(PS5Input input) {
        return input.get(joystick);
    }

    @Override
    public boolean isPressing(PS5Input input) {
        return input.get(joystick) == 1.0;
    }

    @Override
    public boolean isPressingAny(PS5Input[] inputs) {
        for (PS5Input input : inputs) {
            if (isPressing(input)) return true;
        }
        return false;
    }

    @Override
    public boolean isPressingAll(PS5Input[] inputs) {
        for (PS5Input input : inputs) {
            if (!isPressing(input)) return false;
        }
        return true;
    }

    @Override
    public boolean getPressed(PS5Input input) {
        return DriverStation.getStickButtonPressed(joystick.getPort(), input.getButtonID());
    }

    @Override
    public boolean getReleased(PS5Input input) {
        return DriverStation.getStickButtonReleased(joystick.getPort(), input.getButtonID());
    }
}