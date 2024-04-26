package frc.robot.modes;

import org.frc8592.controls.ps5.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopModeManager extends BaseTeleopModeManager {
    private static TeleopModeManager INSTANCE = null;
    public static TeleopModeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeleopModeManager();
        }
        return INSTANCE;
    }

    @Override
    public void runPeriodic() {
        super.updateSwerve();
        super.updateLED();
        PS5Controller dCon = new PS5Controller(0);
        if (dCon.isPressing(PS5Input.MEDIA_BTN)) {
            SmartDashboard.putNumber("AAAADSJLASDLJKKLJAD", 11.0);
        } else {
            SmartDashboard.putNumber("AAAADSJLASDLJKKLJAD", 0.0);
        }

        SmartDashboard.putNumber("BBBASJDKLAJKLSDDAS", dCon.get(PS5Input.RIGHT_TRIGGER_AXIS));
    }
}