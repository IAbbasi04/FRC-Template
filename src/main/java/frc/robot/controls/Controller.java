package frc.robot.controls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public abstract class Controller<T extends ControllerInput> extends GenericHID {
    protected Joystick joystick;

    public Controller(int port) {
        super(port);
        this.joystick = new Joystick(port);
    }

    public abstract double isPressing(T input);

    public abstract double pressed(T input);

    public abstract double released(T input);
}