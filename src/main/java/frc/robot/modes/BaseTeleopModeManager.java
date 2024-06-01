package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.common.Constants;
import lib.frc8592.BooleanManager;
import lib.frc8592.controls.DriveScaler;
import lib.frc8592.controls.DriveScaler.ScaleType;
import frc.robot.controls.InputMap.*;
import frc.robot.subsystems.*;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, false, true);

    protected BooleanManager scoreButton = new BooleanManager();
    protected BooleanManager primeButton = new BooleanManager();

    @Override
    public void runPeriodic() {}

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
    }
}