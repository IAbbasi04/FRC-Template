package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.common.Constants;
import lib.frc8592.controls.DriveScaler;
import lib.frc8592.controls.DriveScaler.ScaleType;
import frc.robot.controls.InputMap.DRIVER;
import frc.robot.subsystems.*;

import lib.frc8592.controls.xbox.XboxInput;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, false);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, false);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, false);

    @Override
    public void runPeriodic() {}

    /**
     * Updates inputs for the swerve throughout the entire teleop mode
     */
    protected void updateSwerve() {
        double desiredX = xScaler.scale(-driverController.get(DRIVER.TRANSLATE_X));
        double desiredY = yScaler.scale(-driverController.get(DRIVER.TRANSLATE_Y));
        double desiredRotate = rotateScaler.scale(-driverController.get(DRIVER.ROTATE));
        boolean snailMode = driverController.get(DRIVER.SNAIL_MODE) == 1.0;
        boolean resetGyro = driverController.get(DRIVER.RESET_GYRO) == 1.0;

        if (resetGyro) { // Reset gyroscopic rotation
            SwerveSubsystem.getInstance().resetGyroscope();
        }

        double translateMultiplier = Constants.SWERVE.TRANSLATE_POWER_FAST;
        double rotateMultiplier = Constants.SWERVE.ROTATE_POWER_FAST;
        if (snailMode) { // Slower translation and rotation
            translateMultiplier = Constants.SWERVE.TRANSLATE_POWER_SLOW;
            rotateMultiplier = Constants.SWERVE.ROTATE_POWER_SLOW;
        }

        desiredX *= translateMultiplier * Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND;
        desiredY *= translateMultiplier * Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND;
        desiredRotate *= rotateMultiplier * Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND;

        ChassisSpeeds speeds = new ChassisSpeeds(desiredX, desiredY, desiredRotate);
        if (Robot.isReal()) speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, SwerveSubsystem.getInstance().getCurrentRotation());
        SwerveSubsystem.getInstance().drive(speeds);

        if (driverController.getPressed(XboxInput.LEFT_BUMPER)) { // Drive to amp
            SwerveSubsystem.getInstance().driveToPose(AutoGenerator.AMP.getPose());
        } else if (driverController.getReleased(XboxInput.LEFT_BUMPER)) {
            SwerveSubsystem.getInstance().driveToPose(null);
        }
    }
}