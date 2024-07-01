package frc.robot.modes;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Controls;
import frc.robot.crescendo.ShotProfile;
import lib.frc8592.controls.DriveScaler;
import lib.frc8592.controls.DriveScaler.ScaleType;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;

public class BaseTeleopModeManager extends ModeManager {
    protected DriveScaler xScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected DriveScaler yScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected DriveScaler rotateScaler = new DriveScaler(ScaleType.LINEAR, false, true);
    protected Controls controls = Robot.CONTROLS;

    private boolean prime = false;
    private ShotProfile desiredShotProfile = new ShotProfile().shouldShoot(false);

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
        // if (Robot.isReal()) speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, SwerveSubsystem.getInstance().getCurrentRotation());
        speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, SwerveSubsystem.getInstance().getCurrentRotation());
        SwerveSubsystem.getInstance().drive(speeds);
    }

    protected void updateIntake() {
        if (controls.INTAKE.getValue()) { // Intake note from ground
            Superstructure.setIntaking();
        } else if (controls.OUTAKE.getValue()) { // Spit note back onto ground
            Superstructure.setOutaking();
        } else { // Ground state
            Superstructure.stopIntake();
            Superstructure.stopFeeder();
        }
    }

    protected void updateShooter() {
        if (controls.PRIME.isRisingEdge()) {
            prime = true;
        }

        if (prime || controls.SCORE.getValue()) { // Priming or scoring
            if (controls.PODIUM_SHOT.getValue()) { // Shoot from podium
                desiredShotProfile = Robot.SHOT_TABLE.getPodiumShot();
            } else if (controls.SUBWOOFER_SHOT.getValue()) { // Shoot from subwoofer
                desiredShotProfile = Robot.SHOT_TABLE.getSubwooferShot();
            } else { // Auto ranged shot
                double distanceToTarget = VisionSubsystem.getInstance().getDistanceToSpeaker();
                desiredShotProfile = Robot.SHOT_TABLE.getShotFromDistance(distanceToTarget);
                if (distanceToTarget == -1) { // Cannot see target
                    desiredShotProfile = Robot.SHOT_TABLE.getStaticShot();
                }
            }
        } else {
            desiredShotProfile = new ShotProfile().shouldShoot(false);
        }

        if (controls.STOW.getValue() || controls.CLIMB_POSITION.getValue()) { // Situations we want to stop priming for
            prime = false;
        }
        
        if (controls.SCORE.getValue()) { // Shoot
            if (ElevatorSubsystem.getInstance().getElevatorState() == ElevatorState.kAmp) { // Amp shot
                if (ElevatorSubsystem.getInstance().atTargetPosition()) { // At the right height
                    Superstructure.scoreAmp();
                } else { // Not at right height yet
                    Superstructure.stopFeeder();
                    Superstructure.stopShooter();
                }
            } else { // Speaker shot
                Superstructure.setShooting(desiredShotProfile);
            }
            prime = false;
        } else if (prime) { // Prepare for shot
            Superstructure.setShooting(desiredShotProfile.shouldShoot(false));
        } else { // Not shooting or primed
            Superstructure.stopShooter();
        }
    }

    protected void updateElevator() {
        if (controls.STOW.getValue() || controls.SCORE.isFallingEdge()) { // Stow elevator
            Superstructure.setGroundState();
        } else if (controls.AMP_POSITION.getValue()) { // Amp position
            Superstructure.setAmpState();
            prime = false;
        } else if (!(controls.SCORE.getValue() || prime)) { // Not attempting to shoot or prime
            if (controls.CLIMB_POSITION.getValue()) { // Start climb
                Superstructure.setClimbState();
            } else if (controls.CLIMB_RAISE.getValue()) { // Climber up
                Superstructure.raiseClimber();
            } else if (controls.CLIMB_LOWER.getValue()) { // Climber down
                Superstructure.lowerClimber();
            }
        } else if (prime || controls.SCORE.getValue()) { // Priming or scoring
            // Do nothing since this is taken care of in the updateShooter() method
        }
    }
}