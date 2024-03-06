package frc.robot.modes;

import static frc.robot.controls.InputMap.*;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.common.Constants;
import frc.robot.controls.DriveScaler;
import frc.robot.controls.DriveScaler.ScaleType;
import frc.robot.modules.DriveModule;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler scaler = new DriveScaler(ScaleType.LINEAR, true); // Linear scaling with a slew limiter

    @Override
    public void runPeriodic() {
        updateSwerve();
    }

    /**
     * Updates inputs for the swerve throughout the entire teleop mode
     */
    protected void updateSwerve() {
        double desiredX = scaler.scale(driverController.get(DRIVER.TRANSLATE_X));
        double desiredY = scaler.scale(driverController.get(DRIVER.TRANSLATE_Y));
        double desiredRotate = scaler.scale(driverController.get(DRIVER.ROTATE));
        boolean snailMode = driverController.get(DRIVER.SNAIL_MODE) == 1.0;
        boolean resetGyro = driverController.get(DRIVER.RESET_GYRO) == 1.0;

        if (resetGyro) { // Reset gyroscopic rotation
            DriveModule.getInstance().resetGyroscope();
        }

        double translateMultiplier = Constants.SWERVE.TRANSLATE_POWER_FAST;
        double rotateMultiplier = Constants.SWERVE.ROTATE_POWER_FAST;
        if (snailMode) { // Slower translation and rotation
            translateMultiplier = Constants.SWERVE.TRANSLATE_POWER_SLOW;
            rotateMultiplier = Constants.SWERVE.ROTATE_POWER_SLOW;
        }

        desiredX *= translateMultiplier;
        desiredY *= translateMultiplier;
        desiredRotate *= rotateMultiplier;

        ChassisSpeeds speeds = new ChassisSpeeds(desiredX, desiredY, desiredRotate);

        speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, DriveModule.getInstance().getCurrentRotation());
        DriveModule.getInstance().drive(speeds);
    }
}