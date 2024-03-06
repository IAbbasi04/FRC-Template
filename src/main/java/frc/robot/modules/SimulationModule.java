package frc.robot.modules;

import frc.robot.common.Enums.MatchMode;

public class SimulationModule extends Module {
    private static SimulationModule INSTANCE = null;
    public static SimulationModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimulationModule();
        }
        return INSTANCE;
    }

    @Override
    public void init(MatchMode mode) {

    }

    @Override
    public void periodic() {

    }
}