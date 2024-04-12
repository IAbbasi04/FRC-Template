package frc.robot.modes;

import com.lib.team8592.BooleanManager;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.controls.DriveScaler;
import frc.robot.controls.EXboxController;
import frc.robot.controls.DriveScaler.ScaleType;
import frc.robot.controls.InputMap.DRIVER;
import frc.robot.controls.InputMap.MANIPULATOR;
import frc.robot.modules.DriveModule;
import frc.robot.modules.ElevatorModule;
import frc.robot.modules.FeederModule;
import frc.robot.modules.IntakeModule;
import frc.robot.modules.VisionModule;
import frc.robot.modules.ElevatorModule.ElevatorState;
import frc.robot.modules.FeederModule.NoteState;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, false);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, false);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, false);

    protected BooleanManager scoreButton = new BooleanManager();
    protected BooleanManager primeButton = new BooleanManager();

    private boolean prime = false;

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

        if (driverController.isPressing(DRIVER.SPEAKER_TARGET_LOCK)) { // Lock angle to speaker
            DriveModule.getInstance().turnToAngle(VisionModule.getInstance().getAngleToSpeaker().getDegrees());
        }

        if (driverController.getPressed(EXboxController.LEFT_BUMPER)) { // Drive to amp
            DriveModule.getInstance().driveToPose(AutoGenerator.AMP.getPose());
        } else if (driverController.getReleased(EXboxController.LEFT_BUMPER)) {
            DriveModule.getInstance().driveToPose(null);
        }
    }

    protected void updateShooter() {
        scoreButton.update(driverController.isPressing(MANIPULATOR.SCORE));
        primeButton.update(driverController.isPressing(MANIPULATOR.PRIME));
        if (primeButton.isRisingEdge()) {
            prime = true;
        }

        ShotProfile desiredShotProfile = new ShotProfile().shouldShoot(false);
        if (driverController.isPressing(MANIPULATOR.MANUAL_MODE)) { // Manual mode
            if (driverController.isPressing(MANIPULATOR.MANUAL_PODIUM_SHOT)) { // Shoot from podium
                desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getPodiumShot();
            } else if (driverController.isPressing(MANIPULATOR.MANUAL_SUBWOOFER_SHOT)) { // Shoot from subwoofer
                desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getSubwooferShot();
            }
        } else { // Auto ranged shot
            double distanceToTarget = VisionModule.getInstance().getDistanceToSpeaker();
            desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(distanceToTarget);
            if (distanceToTarget == -1) { // Cannot see target
                desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getStaticShot();
            }
        }

        if (driverController.isPressingAny(MANIPULATOR.STOW, MANIPULATOR.CLIMB_POSITION)) { // Situations we want to stop priming for
            prime = false;
        }
        
        if (scoreButton.getValue()) { // Shoot
            if (ElevatorModule.getInstance().getElevatorState() == ElevatorState.kAmp) { // Amp shot
                ModeManager.scoreAmp();
            } else { // Speaker shot
                ModeManager.setShooting(desiredShotProfile);
            }
            prime = false;
        } else if (prime) { // Prepare for shot
            setShooting(desiredShotProfile.shouldShoot(false));
        } else { // Not shooting or primed
            stopShooter();
        }
    }

    protected void updateIntake() {
        if (!operatorController.isPressing(MANIPULATOR.SCORE)) { // Not scoring
            if (operatorController.isPressing(MANIPULATOR.MANUAL_MODE)) {  // Manual controls
                if (operatorController.isPressing(MANIPULATOR.INTAKE)) { // Intake a note
                    FeederModule.getInstance().setFeederVelocity(Constants.FEEDER.FEEDER_INTAKE_RPM);
                    IntakeModule.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
                } else if (operatorController.isPressing(MANIPULATOR.OUTAKE)) { // Spit out a note
                    FeederModule.getInstance().setFeederVelocity(Constants.FEEDER.FEEDER_OUTAKE_RPM);
                    IntakeModule.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTAKE_RPM);
                } else { // Not pressing anything
                    super.stopFeeder();
                    super.stopIntake();
                }
            } else if (operatorController.isPressing(MANIPULATOR.INTAKE)) { // Intake a note
                super.setIntaking();
            } else if (operatorController.isPressing(MANIPULATOR.OUTAKE)) { // Spit out a note
                super.setOutaking();
            } else { // Not pressing anything
                super.stopIntake();
                if (FeederModule.getInstance().getNoteState() == NoteState.kNone ||
                    operatorController.isPressing(MANIPULATOR.STOW) ||
                    scoreButton.isFallingEdge()) {
                    super.stopFeeder();
                }
            }
        }
    }

    protected void updateElevator() {
        if (operatorController.isPressing(MANIPULATOR.STOW) || scoreButton.isFallingEdge()) { // Stow elevator
            super.setGroundState();
        } else if (operatorController.isPressing(MANIPULATOR.AMP_POSITION)) { // Amp position
            super.setAmpState();
            prime = false;
        } else if (!(operatorController.isPressing(MANIPULATOR.SCORE) || prime)) { // Not attempting to shoot or prime
            if (operatorController.isPressing(MANIPULATOR.CLIMB_POSITION)) { // Start climb
                super.setClimbState();
            } else if (operatorController.isPressing(MANIPULATOR.EXTENSION_RAISE)) { // Climber up
                super.raiseClimber();
            } else if (operatorController.isPressing(MANIPULATOR.EXTENSION_LOWER)) { // Climber down
                super.lowerClimber();
            }
        }
    }
}