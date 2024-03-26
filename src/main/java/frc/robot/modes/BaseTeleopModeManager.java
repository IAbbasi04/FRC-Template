package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.common.BooleanManager;
import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.controls.DriveScaler;
import frc.robot.controls.DriveScaler.ScaleType;
import frc.robot.controls.InputMap.DRIVER;
import frc.robot.controls.InputMap.MANIPULATOR;
import frc.robot.modules.DriveModule;
import frc.robot.modules.VisionModule;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, true);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, true);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, true);

    protected BooleanManager scoreButton = new BooleanManager();
    protected BooleanManager primeButton = new BooleanManager();

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
    }

    protected void updateShooter() {
        scoreButton.update(driverController.isPressing(MANIPULATOR.SCORE));

        ShotProfile desiredShotProfile = new ShotProfile().shouldShoot(false);
        if (driverController.isPressing(MANIPULATOR.MANUAL_MODE)) { // Manual shots
            if (driverController.isPressing(MANIPULATOR.MANUAL_SUBWOOFER_SHOT)) { // Shoot from subwoofer
                desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(1.40);
            } else if (driverController.isPressing(MANIPULATOR.MANUAL_PODIUM_SHOT)) { // Shoot from podium
                desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(2.83);
            }
        } else if (driverController.isPressing(MANIPULATOR.SCORE) || driverController.isPressing(MANIPULATOR.PRIME)) { // Auto ranged shot
            double distanceToTarget = VisionModule.getInstance().getDistanceToSpeaker();
            desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(distanceToTarget);
        }

        if (scoreButton.isFallingEdge() || driverController.isPressingAny(MANIPULATOR.STOW, MANIPULATOR.INTAKE, MANIPULATOR.OUTAKE)) { // Stop shooting
            super.stopShooter();
        } else { // Shoot
            super.setShooting(desiredShotProfile);
        }
    }

    protected void updateIntake() {
        if (driverController.isPressing(MANIPULATOR.INTAKE)) {
            super.setIntaking();
        } else if (driverController.isPressing(MANIPULATOR.OUTAKE)) {
            super.setOutaking();
        } else {
            super.stopAllRollers();
        }
    }
}