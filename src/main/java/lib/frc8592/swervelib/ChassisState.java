package lib.frc8592.swervelib;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class ChassisState {
    public ChassisSpeeds currentSpeeds;
    public Pose2d currentPose;

    public ChassisState(ChassisSpeeds currentSpeeds, Pose2d currentPose) {
        this.currentSpeeds = currentSpeeds;
        this.currentPose = currentPose;
    }
}