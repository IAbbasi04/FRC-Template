package frc.robot.modes;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.regressions.QuadraticRegression;

public class DisabledModeManager extends ModeManager {
    private static DisabledModeManager INSTANCE = null;
    public static DisabledModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DisabledModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        double[] inputs = new double[]{-3, -2, -1, 0, 1, 2, 3};
        double[] outputs = new double[]{7.5, 3, 0.5, 1, 3, 6, 14};

        QuadraticRegression regression = new QuadraticRegression(inputs, outputs);
        SmartDashboard.putString("REGRESSION", regression.toString());
    }
}