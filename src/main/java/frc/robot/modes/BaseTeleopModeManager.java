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
        if (controls.INTAKE.getValue()) { // Intake note
            Superstructure.setIntaking();
        } else if (controls.PLACE.getValue()) { // Place note on ground
            Superstructure.setPlacing();
        } else { // Do nothing in ground state
            Superstructure.setIntakeGroundState();
        }
    }

    protected void updateShooter() {
        if (controls.PRIME_SUBWOOFER.getValue()) { // Score in speaker against subwoofer
            // Set the shot profile to subwoofer shot
        } else if (controls.PRIME_PODIUM.getValue()) { // Score in speaker against podium
            // Set the shot profile to podium shot
        } else { // Score from anywhere
            // Set the shot profile to ranged shot
        }

        if (controls.SCORE.getValue()) {
            // Ask for if we want amp or speaker
            // Shoot note or score in amp
        } else {
            // Disable shooter
        }
    }
    
    protected void updateBackpack() {
        if (controls.AMP.isFallingEdge()) { // Set to amp state
            Superstructure.loadAmp();
        } else if (controls.RUN_CLIMB.getValue()) { // 

        }
    }

    protected void updateClimber() {

    }
}