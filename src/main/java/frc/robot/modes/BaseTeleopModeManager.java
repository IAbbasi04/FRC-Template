package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.common.Constants;
import frc.robot.common.crescendo.ShotProfile;
import lib.frc8592.BooleanManager;
import lib.frc8592.controls.DriveScaler;
import lib.frc8592.controls.DriveScaler.ScaleType;
import frc.robot.controls.InputMap.DRIVER;
import frc.robot.controls.InputMap.MANIPULATOR;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;
import frc.robot.subsystems.FeederSubsystem.NoteState;
import lib.frc8592.controls.xbox.XboxInput;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, false);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, false);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, false);

    protected BooleanManager scoreButton = new BooleanManager();
    protected BooleanManager primeButton = new BooleanManager();

    private boolean prime = false;

    private Superstructure superstructure;

    @Override
    public void runPeriodic() {
        if (superstructure == null) superstructure = Superstructure.getInstance();
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
            double distanceToTarget = VisionSubsystem.getInstance().getDistanceToSpeaker();
            desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(distanceToTarget);
            if (distanceToTarget == -1) { // Cannot see target
                desiredShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getStaticShot();
            }
        }

        if (driverController.isPressingAny(new XboxInput[]{MANIPULATOR.STOW, MANIPULATOR.CLIMB_POSITION})) { // Situations we want to stop priming for
            prime = false;
        }
        
        if (scoreButton.getValue()) { // Shoot
            if (ElevatorSubsystem.getInstance().getElevatorState() == ElevatorState.kAmp) { // Amp shot
                superstructure.scoreAmp();
            } else { // Speaker shot
                superstructure.setShooting(desiredShotProfile);
            }
            prime = false;
        } else if (prime) { // Prepare for shot
            superstructure.setShooting(desiredShotProfile.shouldShoot(false));
        } else { // Not shooting or primed
            superstructure.stopShooter();
        }
    }

    protected void updateIntake() {
        if (!operatorController.isPressing(MANIPULATOR.SCORE)) { // Not scoring
            if (operatorController.isPressing(MANIPULATOR.MANUAL_MODE)) {  // Manual controls
                if (operatorController.isPressing(MANIPULATOR.INTAKE)) { // Intake a note
                    FeederSubsystem.getInstance().setFeederVelocity(Constants.FEEDER.FEEDER_INTAKE_RPM);
                    IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
                } else if (operatorController.isPressing(MANIPULATOR.OUTAKE)) { // Spit out a note
                    FeederSubsystem.getInstance().setFeederVelocity(Constants.FEEDER.FEEDER_OUTAKE_RPM);
                    IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTAKE_RPM);
                } else { // Not pressing anything
                    superstructure.stopFeeder();
                    superstructure.stopIntake();
                }
            } else if (operatorController.isPressing(MANIPULATOR.INTAKE)) { // Intake a note
                superstructure.setIntaking();
            } else if (operatorController.isPressing(MANIPULATOR.OUTAKE)) { // Spit out a note
                superstructure.setOutaking();
            } else { // Not pressing anything
                superstructure.stopIntake();
                if (FeederSubsystem.getInstance().getNoteState() == NoteState.kNone ||
                    operatorController.isPressing(MANIPULATOR.STOW) ||
                    scoreButton.isFallingEdge()) {
                    superstructure.stopFeeder();
                }
            }
        }
    }

    protected void updateElevator() {
        if (operatorController.isPressing(MANIPULATOR.STOW) || scoreButton.isFallingEdge()) { // Stow elevator
            superstructure.setGroundState();
        } else if (operatorController.isPressing(MANIPULATOR.AMP_POSITION)) { // Amp position
            superstructure.setAmpState();
            prime = false;
        } else if (!(operatorController.isPressing(MANIPULATOR.SCORE) || prime)) { // Not attempting to shoot or prime
            if (operatorController.isPressing(MANIPULATOR.CLIMB_POSITION)) { // Start climb
                superstructure.setClimbState();
            } else if (operatorController.isPressing(MANIPULATOR.EXTENSION_RAISE)) { // Climber up
                superstructure.raiseClimber();
            } else if (operatorController.isPressing(MANIPULATOR.EXTENSION_LOWER)) { // Climber down
                superstructure.lowerClimber();
            }
        }
    }

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