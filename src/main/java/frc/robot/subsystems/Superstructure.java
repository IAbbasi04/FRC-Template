package frc.robot.subsystems;

import frc.robot.common.Constants;
import frc.robot.subsystems.IndexerSubsystem.IndexingState;

public class Superstructure {
    /**
     * Intake a note from the ground
     */
    public static void setIntaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(3000);
        IntakeSubsystem.getInstance().setArmAngle(60.0);
        IndexerSubsystem.getInstance().setIndexingState(IndexingState.kIntake);
    }

    /**
     * Place a note in front of the robot
     */
    public static void setPlacing() {
        IntakeSubsystem.getInstance().setRollerVelocity(-3000);
        IntakeSubsystem.getInstance().setArmAngle(60.0);
        if (IntakeSubsystem.getInstance().getArmPosition() >= 45) {
            // Only spit note if arm past certain threshold
            IndexerSubsystem.getInstance().setIndexingState(IndexingState.kPlace);
        } else {
            IndexerSubsystem.getInstance().setIndexingState(IndexingState.kOff);
        }
    }

    /**
     * Turns off intake motor and feeder motor
     */
    public static void setIntakeRollersOff() {
        IntakeSubsystem.getInstance().setRollerVelocity(0.0);
    }

    /**
     * Stop spinning the intake and retract the arm
     */
    public static void setIntakeGroundState() {
        setIntakeRollersOff();
        IntakeSubsystem.getInstance().setArmAngle(0.0);
        IndexerSubsystem.getInstance().setIndexingState(IndexingState.kOff);
    }

    /**
     * Passes note into amp mechanism
     */
    public static void loadAmp() {
        if (IndexerSubsystem.getInstance().hasNote()) { // Note in robot
            IndexerSubsystem.getInstance().setFeederVelocity(Constants.INDEXER.FEEDER_AMP_PASS_VELOCITY_RPM);
            IndexerSubsystem.getInstance().setIndexerVelocity(Constants.INDEXER.INDEXER_AMP_PASS_VELOCITY_RPM);
        } else { // No note in robot
            setIntaking();
        }
    }

    /**
     * Prepares the amp mechanism 
     */
    public static void stageAmp() {
        if (IndexerSubsystem.getInstance().hasNote()) { // Only load if backpack currently loaded

        }
    }

    /**
     * Scores note in the amp
     */
    public static void scoreAmp() {
        
    }
}