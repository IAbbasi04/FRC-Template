package frc.robot.modules;

import frc.robot.common.Enums.MatchMode;

public class IntakeModule extends Module {
    private static IntakeModule INSTANCE = null;
    public static IntakeModule getInstance() {
        if (INSTANCE == null) INSTANCE = new IntakeModule();
        return INSTANCE;
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}