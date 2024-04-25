package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.common.Constants;
import frc.robot.controls.DriveScaler;
import frc.robot.controls.DriveScaler.ScaleType;
import frc.robot.controls.InputMap.DRIVER;
import frc.robot.controls.xbox.XboxInput;
import frc.robot.modules.*;

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
            DriveModule.getInstance().resetGyroscope();
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
        if (Robot.isReal()) speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, DriveModule.getInstance().getCurrentRotation());
        DriveModule.getInstance().drive(speeds);

        if (driverController.getPressed(XboxInput.LEFT_BUMPER)) { // Drive to amp
            DriveModule.getInstance().driveToPose(AutoGenerator.AMP.getPose());
        } else if (driverController.getReleased(XboxInput.LEFT_BUMPER)) {
            DriveModule.getInstance().driveToPose(null);
        }
    }

    /**
     * Periodically updates inputs for the example subsytem
     */
    public void updateExample() {
        if (driverController.isPressing(XboxInput.RIGHT_TRIGGER_BTN)) { // Right trigger pressed
            ExampleModule.getInstance().setDesiredVelocity(2000.0);
        } else if (driverController.isPressing(XboxInput.LEFT_TRIGGER_BTN)) {
            ExampleModule.getInstance().setDesiredVelocity(-2000.0);
        } else {
            ExampleModule.getInstance().setDesiredVelocity(0.0);
        }
    }
}