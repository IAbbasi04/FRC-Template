package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Controls;
import lib.frc8592.controls.DriveScaler;
import lib.frc8592.controls.DriveScaler.ScaleType;
import frc.robot.subsystems.*;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected Controls controls = Robot.CONTROLS;

    @Override
    public void runPeriodic() {}

    protected void updateSwerve() {
        double desiredX = xScaler.scale(controls.SWERVE_TRANSLATE_X);
        double desiredY = yScaler.scale(controls.SWERVE_TRANSLATE_Y);
        double desiredRotate = rotateScaler.scale(controls.SWERVE_ROTATE);

        if (controls.RESET_GYRO.getValue()) { // Reset gyroscopic rotation
            SwerveSubsystem.getInstance().resetGyroscope();
        }

        double translateMultiplier = Constants.SWERVE.TRANSLATE_POWER_FAST;
        double rotateMultiplier = Constants.SWERVE.ROTATE_POWER_FAST;
        if (controls.SNAIL_MODE.getValue()) { // Slower translation and rotation
            translateMultiplier = Constants.SWERVE.TRANSLATE_POWER_SLOW;
            rotateMultiplier = Constants.SWERVE.ROTATE_POWER_SLOW;
        }

        desiredX *= translateMultiplier * Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND;
        desiredY *= translateMultiplier * Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND;
        desiredRotate *= rotateMultiplier * Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND;

        ChassisSpeeds speeds = new ChassisSpeeds(desiredX, desiredY, desiredRotate);
        if (Robot.isReal()) speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, SwerveSubsystem.getInstance().getCurrentRotation());
        SwerveSubsystem.getInstance().drive(speeds);
    }

    protected void updateIntake() {
        if (controls.INTAKE.getValue()) { // Intake note from ground
            Superstructure.setIntaking();
        } else if (controls.OUTAKE.getValue()) { // Spit note back onto ground
            Superstructure.setOutaking();
        } else { // Ground state
            Superstructure.stopIntake();
        }
    }
}