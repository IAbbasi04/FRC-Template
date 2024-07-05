package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.crescendo.ShotProfile;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;
import frc.robot.subsystems.FeederSubsystem.FeederState;
import lib.frc8592.MatchMode;
import lib.frc8592.logging.SmartLogger;
import frc.robot.common.Constants;

/**
 * Class designed for holding inter-mechanism code and logic
 * 
 * This class is used in all match modes for increased consistency
 * and reliability between all modes
 * 
 * Typically, game functions only requiring a single mechanism 
 * can be dealt with inside its respective mechanism class
 */
public class Superstructure extends Subsystem {
    private static Superstructure INSTANCE = null;
    public static Superstructure getInstance() {
        if (INSTANCE == null) INSTANCE = new Superstructure();
        return INSTANCE;
    }

    public enum Superstate {
        kDefault,
        kIntake,
        kOutake,
        kClimb,
        kPrime,
        kScore,
        kStow,
        ;
    }

    public enum ScoreState {
        kOverride(),
        kNone(Robot.SHOT_TABLE.getStaticShot()),
        kRanged,
        kSubwoofer(Robot.SHOT_TABLE.getSubwooferShot()),
        kPodium(Robot.SHOT_TABLE.getPodiumShot()),
        kAmp,
        kPass(Robot.SHOT_TABLE.getPassShot());

        public double leftRPM = 0;
        public double rightRPM = 0;
        public double pivot = 0;
        public double extension = 0;

        private ScoreState() {}

        private ScoreState(double leftRPM, double rightRPM, double pivot, double extension) {
            this.leftRPM = leftRPM;
            this.rightRPM = rightRPM;
            this.pivot = pivot;
            this.extension = extension;
        }

        private ScoreState(ShotProfile shotprofile) {
            this.leftRPM = shotprofile.leftShotRPM;
            this.rightRPM = shotprofile.rightShotRPM;
            this.pivot = shotprofile.pivotDegrees;
            this.extension = shotprofile.extensionMeters;
        }
    }

    private Superstate state = Superstate.kDefault;
    private ScoreState scoreState = ScoreState.kNone;

    private ShotProfile overrideShotProfile = new ShotProfile();

    private Superstructure() {
        super.logger = new SmartLogger("Superstructure");
    }

    /**
     * Sets the state of the robot and all mechanisms
     */
    public void setSuperState(Superstate state) {
        this.state = state;
    }

    /**
     * Sets the scoring state of the robot
     */
    public void setScoreState(ScoreState state) {
        this.scoreState = state;
    }

    /**
     * The desired state of the robot
     */
    public Superstate getState() {
        return this.state;
    }
    
    @Override
    public void init(MatchMode mode) {
        this.state = Superstate.kDefault; // Anytime we switch between modes, reset superstate
    }

    @Override
    public void initializeLogs() {
        logger.logEnum("Superstructure State", () -> getState());
    }

    @Override
    public void periodic() {
        switch (state) {
            case kIntake:
                IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_INTAKE_RPM);
                FeederSubsystem.getInstance().setFeederState(FeederState.kIntake);
                ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kStow);
                break;
            case kOutake:
                IntakeSubsystem.getInstance().setRollerVelocity(Constants.INTAKE.ROLLER_OUTAKE_RPM);
                FeederSubsystem.getInstance().setFeederState(FeederState.kOutake);
                break;
            case kScore:
                switch (scoreState) {
                    case kAmp:
                        // Amp scoring does not require shooter to be up to speed
                        FeederSubsystem.getInstance().setFeederVelocity(Constants.FEEDER.FEEDER_AMP_RPM);
                        break;
                    default:
                        if (ShooterSubsystem.getInstance().isAtTargetSpeed()) {
                            // Only shoot if shooter up to speed
                            FeederSubsystem.getInstance().setFeederState(FeederState.kShoot);
                        } else {
                            FeederSubsystem.getInstance().setFeederVelocity(0.0);
                        }
                        break;
                }
                // Fall through since scoring is just an extension of priming
            case kPrime:
                switch (scoreState) {
                    case kRanged:
                        double distanceToTarget = VisionSubsystem.getInstance().getDistanceToSpeaker();
                        ShotProfile profile = Robot.SHOT_TABLE.getShotFromDistance(distanceToTarget);
                        // SmartDashboard.putNumber("JKLASDAJKLSDALJKDASJKL", profile.pivotDegrees);
                        ShooterSubsystem.getInstance().setDesiredVelocity(profile.leftShotRPM, profile.rightShotRPM);
                        ElevatorSubsystem.getInstance().setPivot(profile.pivotDegrees);
                        ElevatorSubsystem.getInstance().setExtension(profile.extensionMeters);
                        break;
                    case kAmp:
                        ShooterSubsystem.getInstance().setDesiredVelocity(-1000, -1000);
                        ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kAmp);
                        break;
                    case kOverride:
                        ShooterSubsystem.getInstance().setDesiredVelocity(
                            overrideShotProfile.leftShotRPM, 
                            overrideShotProfile.rightShotRPM
                        );
                        ElevatorSubsystem.getInstance().setPivot(overrideShotProfile.pivotDegrees);
                        ElevatorSubsystem.getInstance().setExtension(overrideShotProfile.extensionMeters);
                    default:
                        ShooterSubsystem.getInstance().setDesiredVelocity(scoreState.leftRPM, scoreState.rightRPM);
                        ElevatorSubsystem.getInstance().setPivot(scoreState.pivot);
                        ElevatorSubsystem.getInstance().setExtension(scoreState.extension);
                        break;
                }
                break;
            case kClimb:
                ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kClimb);
                break;
            case kStow:
                ElevatorSubsystem.getInstance().setElevatorState(ElevatorState.kStow);
                // Stow is basically default but with a grounded elevator system
                // So we fall through here
            case kDefault:
                // Fall through intentional
            default:
                // No rollers spinning in a default state
                IntakeSubsystem.getInstance().setRollerVelocity(0.0);
                ShooterSubsystem.getInstance().setDesiredVelocity(0, 0);
                FeederSubsystem.getInstance().setFeederState(FeederState.kOff);
                break;
        }
    }

    /**
     * Sets a desired shot profile to be directly applied
     */
    public void setShotProfile(ShotProfile shotProfile) {
        this.scoreState = ScoreState.kOverride;
        this.overrideShotProfile = shotProfile;
        if (shotProfile.shouldShoot) { // Score
            this.state = Superstate.kScore;
        } else { // Prime
            this.state = Superstate.kPrime;
        }
    }

    /**
     * Extends the elevator
     */
    public void raiseClimber() {
        ElevatorSubsystem.getInstance().moveElevator(0.005);
    }

    /**
     * Retracts the elevator
     */
    public void lowerClimber() {
        ElevatorSubsystem.getInstance().moveElevator(-0.005);
    }
}