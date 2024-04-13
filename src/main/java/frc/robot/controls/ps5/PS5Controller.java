package frc.robot.controls.ps5;

import frc.robot.controls.Controller;

public class PS5Controller extends Controller<PS5Input> {
    public PS5Controller(int port) {
        super(port);
    }

    @Override
    public double isPressing(PS5Input input) {
        return 0;
    }

    @Override
    public double pressed(PS5Input input) {
        return 0;
    }

    @Override
    public double released(PS5Input input) {
        return 0;
    }
}