package frc.robot.subsystems;

import frc.robot.common.*;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.BeamSensor;
import lib.frc8592.hardware.motors.KrakenMotor;
import lib.frc8592.hardware.motors.Motor;
import lib.frc8592.logging.SmartLogger;

import static frc.robot.common.Constants.INDEXER.*;

public class IndexerSubsystem extends Subsystem {
    private static IndexerSubsystem INSTANCE = null;
    public static IndexerSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new IndexerSubsystem();
        return INSTANCE;
    }

    public enum NoteState {
        kNone,
        kHasNote,
        KAlinging,
        kStaged,
    }

    public enum IndexingState {
        kOff,
        kIntake,
        kShoot,
        kPlace,
        kOverride,
        kAmp
    }

    private Motor feederMotor;
    private Motor indexerMotor;

    private BeamSensor frontSensor;
    private BeamSensor backSensor;

    private double desiredFeederVelocityRPM = 0.0;
    private double desiredIndexerVelocityRPM = 0.0;

    private NoteState noteState = NoteState.kNone;
    private IndexingState indexingState = IndexingState.kOff;

    private IndexerSubsystem() {
        feederMotor = new KrakenMotor(Ports.FEEDER_CAN_ID);
        indexerMotor = new KrakenMotor(Ports.INDEXER_CAN_ID);

        frontSensor = new BeamSensor(Ports.FRONT_BEAM_CAN_ID);
        backSensor = new BeamSensor(Ports.BACK_BEAM_CAN_ID);

        super.logger = new SmartLogger("IndexerSubsystem");
    }

    /**
     * Sets the desired mode for the indexing system
     */
    public void setIndexingState(IndexingState indexingState) {
        this.indexingState = indexingState;
    }

    /**
     * Sets the speed of the feeder motor in RPM
     */
    public void setFeederVelocity(double desiredVelocityRPM) {
        this.desiredFeederVelocityRPM = desiredVelocityRPM;
        this.indexingState = IndexingState.kOverride;
    }

    /**
     * Sets the speed of the feeder motor in RPM
     */
    public void setIndexerVelocity(double desiredVelocityRPM) {
        this.desiredIndexerVelocityRPM = desiredVelocityRPM;
        this.indexingState = IndexingState.kOverride;
    }

    /**
     * The current speed of the feeder motor in RPM
     */
    public double getFeederVelocity() {
        return this.feederMotor.getVelocity();
    }

    /**
     * The current speed of the indexer motor in RPM
     */
    public double getIndexerVelocity() {
        return this.indexerMotor.getVelocity();
    }

    /**
     * Whether the front beam sensor has been tripped 
     */
    public boolean isFrontBeamTripped() {
        return this.frontSensor.isBroken();
    }

    /**
     * Whether the back beam sensor has been tripped 
     */
    public boolean isBackBeamTripped() {
        return this.backSensor.isBroken();
    }

    /**
     * Get the current state of the note in the robot if there is one
     */
    public NoteState getNoteState() {
        return this.noteState;
    }

    /**
     * There is a note in the robot
     */
    public boolean hasNote() {
        return this.noteState != NoteState.kNone;
    }

    /**
     * Note is in desired position in the robot
     */
    public boolean isNoteStaged() {
        return this.noteState == NoteState.kStaged;
    }

    /**
     * Get the current state of the indexing system
     */
    public IndexingState getIndexingState() {
        return this.indexingState;
    }

    @Override
    public void init(MatchMode mode) {
        this.setIndexingState(IndexingState.kOff);
        this.setIndexerVelocity(0.0);
    }

    @Override
    public void initializeLogs() {
        logger.logDouble("Desired Feeder Velocity (RPM)", () -> desiredFeederVelocityRPM);
        logger.logDouble("Desired Indexer Velocity (RPM)", () -> desiredIndexerVelocityRPM);
        logger.logDouble("Current Feeder Velocity (RPM)", () -> getFeederVelocity());
        logger.logDouble("Current Indexer Velocity (RPM)", () -> getFeederVelocity());

        logger.logBoolean("Front Beam Tripped", () -> isFrontBeamTripped());
        logger.logBoolean("Back Beam Tripped", () -> isBackBeamTripped());

        logger.logEnum("Note State", () -> getNoteState());
        logger.logEnum("Feeder State", () -> getIndexingState());
    }

    @Override
    public void periodic() {
        if (isFrontBeamTripped()) { // Note detected in indexing system
            if (isBackBeamTripped()) { // Note staged in the back
                this.noteState = NoteState.kStaged;
            } else { // Note has not been staged yet
                this.noteState = NoteState.kHasNote;
            }
        } else if (isBackBeamTripped()) { // Note passed too far
            this.noteState = NoteState.KAlinging;
        } else {
            // Assume if neither beam break sees the note
            // There isn't one in the robot
            this.noteState = NoteState.kNone;
        }

        if (indexingState != IndexingState.kOverride) {
            // Reset when in a different state
            desiredFeederVelocityRPM = 0.0;
            desiredIndexerVelocityRPM = 0.0;
        }

        double appliedFeederVelocityRPM = desiredFeederVelocityRPM;
        double appliedIndexerVelocityRPM = desiredIndexerVelocityRPM;

        switch(indexingState) {
            case kIntake:
                switch(noteState) {
                    case kNone:
                        // Don't spin indexer until note acquired
                        // Otherwise whether or not front beam tripped
                        // spin feeder at same speed
                        appliedIndexerVelocityRPM = 0.0;
                    case kHasNote:
                        appliedFeederVelocityRPM = FEEDER_INTAKE_VELOCITY_RPM;
                        appliedIndexerVelocityRPM = INDEXER_INTAKE_VELOCITY_RPM;
                        break;
                    case KAlinging:
                        appliedFeederVelocityRPM = FEEDER_ALIGN_VELOCITY_RPM;
                        appliedIndexerVelocityRPM = INDEXER_ALIGN_VELOCITY_RPM;
                        break;
                    case kStaged: 
                    // Fall through intentional since this also just turns off feeder
                    default:
                        appliedFeederVelocityRPM = 0.0;
                        appliedIndexerVelocityRPM = 0.0;
                        break;
                }
                break;
            case kOverride:
                // Do nothing since the applied is already set to the override speed
                break;
            case kAmp:
                if (isBackBeamTripped()) { // Too far back
                     // Need to push forward to allow amp mech to spin up
                    appliedFeederVelocityRPM = FEEDER_AMP_INITIATE_VELOCITY_RPM;
                    appliedIndexerVelocityRPM = INDEXER_AMP_INITIATE_VELOCITY_RPM;
                } else {
                    appliedFeederVelocityRPM = FEEDER_AMP_PASS_VELOCITY_RPM;
                    appliedIndexerVelocityRPM = INDEXER_AMP_PASS_VELOCITY_RPM;
                }
                break;
            case kPlace:
                appliedFeederVelocityRPM = FEEDER_PLACE_VELOCITY_RPM;
                appliedIndexerVelocityRPM = INDEXER_PLACE_VELOCITY_RPM;
                break;
            case kShoot:
                appliedFeederVelocityRPM = FEEDER_SHOOT_VELOCITY_RPM;
                appliedIndexerVelocityRPM = INDEXER_SHOOT_VELOCITY_RPM;
                break;
            case kOff:
                if (isFrontBeamTripped()) { // Note in robot so switch to intaking
                    this.indexingState = IndexingState.kIntake;
                }
                // Fall through intentionally
            default:
                appliedFeederVelocityRPM = 0.0;
                appliedIndexerVelocityRPM = 0.0;
                break;
        }

        feederMotor.setVelocity(appliedFeederVelocityRPM);
        indexerMotor.setVelocity(appliedIndexerVelocityRPM);
    }
}