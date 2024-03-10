package frc.robot.modules;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.Limelight;

public class VisionModule extends Module {
    private static VisionModule INSTANCE = null;
    public static VisionModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionModule();
        }
        return INSTANCE;
    }

    private Limelight limelight;

    /**
     * The estimated pose based on limelight vision of april tag
     */
    public Pose2d getLimelightEstimatedPose() {
        return new Pose2d();
    }

    /**
     * Whether an april tag is visible
     */
    public boolean isAprilTagTargetValid() {
        return limelight.isTargetValid();
    }

    private VisionModule() {
        limelight = new Limelight("limelight-targetting");
    }

    @Override
    public void init(MatchMode mode) {

    }

    @Override
    public void periodic() {
        if (limelight.isTargetValid()) {
            SmartDashboard.putNumber("TX", limelight.getX());
        }
    }
}