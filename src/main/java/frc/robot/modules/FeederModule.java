package frc.robot.modules;

import com.lib.team1885.BeamSensor;
import com.lib.team1885.ProfileGains;
import com.lib.team8592.hardware.motors.*;
import com.lib.team8592.hardware.motors.Motor.ControlType;

import frc.robot.Robot;
import frc.robot.common.*;
import frc.robot.common.Enums.MatchMode;

public class FeederModule extends Module {
    private static FeederModule INSTANCE = null;
    public static FeederModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FeederModule();
        }
        return INSTANCE;
    }

    private NoteState noteState;
    private FeederState feederState;

    private VortexMotor feederMotor;
    private BeamSensor entryBeam;
    private BeamSensor exitBeam;
    private double desiredRPM;
    private boolean hasNote;

    private ProfileGains feederShootGains = new ProfileGains()
        .setP(0.001)
        .setFF(0.00018)
    ;

    private ProfileGains feederFeedGains = new ProfileGains()
        .setP(0.00001)
        .setFF(0.00018)
    ;

    public enum NoteState {
        kNone,
        kHasNote,
        kAligning,
        kStaged
    }

    public enum FeederState {
        kOff,
        kIntake,
        kShoot,
        kOutake,
        kOverride,
    }

    private FeederModule() {
        feederMotor = new VortexMotor(Ports.FEEDER_MOTOR_CAN_ID);
        feederMotor.withGains(feederFeedGains, Constants.FEEDER.PID_FEED_SLOT);
        feederMotor.withGains(feederShootGains, Constants.FEEDER.PID_SHOOT_SLOT);

        entryBeam = new BeamSensor(Ports.ENTRY_BEAM_DIO_PORT);
        exitBeam = new BeamSensor(Ports.EXIT_BEAM_DIO_PORT);

        hasNote = false;
        noteState = NoteState.kNone;
        feederState = FeederState.kOff;
        super.addLogger("Feeder", Robot.LOG_TO_DASHBOARD);
    }

    /**
     * Sets the RPM demand of the feeder motor
     */
    public void setFeederVelocity(double rpm) {
        this.desiredRPM = rpm;
        this.feederState = FeederState.kOverride;
    }

    /**
     * Current speed of the feeder motor in RPM
     */
    public double getFeederVelocity() {
        return this.feederMotor.getVelocity();
    }

    /**
     * The state of whether there is a note in the robot and where it is
     */
    public NoteState getNoteState() {
        return noteState;
    }

    /**
     * The state of the feeder
     */
    public FeederState getFeederState() {
        return feederState;
    }

    /**
     * Sets desired state of the feeder
     */
    public void setFeederState(FeederState state) {
        this.feederState = state;
    }

    /**
     * Whether there is a note in the robot
     */
    public boolean hasNote() {
        return this.hasNote;
    }

    /**
     * Whether there is a note in the robot
     */
    public boolean noteStaged() {
        return this.noteState == NoteState.kStaged;
    }

    /**
     * Removes declaration that the robot has a note
     */
    public void resetHasNote() {
        this.hasNote = false;
    }

    /**
     * Beam broken for entry sensor
     */
    private boolean entryBroken() {
        return entryBeam.isBroken();
    }

    /**
     * Beam broken for entry sensor
     */
    private boolean exitBroken() {
        return exitBeam.isBroken();
    }


    /**
     * Desired RPM for the feeder motor
     */
    private double getDesiredRPM() {
        return desiredRPM;
    }

    @Override
    public void init(MatchMode mode) {
        feederMotor.set(ControlType.kVelocity, 0, Constants.FEEDER.PID_FEED_SLOT);
    }

    @Override
    public void initializeLogs() {
        logger.setNumber("Applied RPM", () -> getDesiredRPM());

        logger.setBoolean("Entry Broken", () -> entryBroken());
        logger.setBoolean("Exit Broken", () -> exitBroken());
        logger.setBoolean("Has Note", () -> hasNote());

        logger.setEnum("Feeder State", () -> getFeederState());
        logger.setEnum("Note State", () -> getNoteState());
    }

    @Override
    public void periodic() {
        if (this.exitBroken()) { // Any point the exit beam is broken align the note with the entry beam
            this.noteState = NoteState.kAligning;
        } else if (this.entryBroken()) {
            if (noteState.equals(NoteState.kNone)) { // Note not previously in robot
                this.hasNote = true;
                this.noteState = NoteState.kHasNote;
            } else if (noteState.equals(NoteState.kAligning)) { // Align until the note hits the entry beam sensor
                this.noteState = NoteState.kStaged;
            }
        }

        if (this.noteState == NoteState.kStaged) {
            if (!this.entryBroken() && !this.exitBroken()) { // No beam tripped
                this.noteState = NoteState.kNone;
            }
        }
        
        int pidSlot = Constants.FEEDER.PID_FEED_SLOT;


        switch (feederState) {
            case kOff:
                desiredRPM = 0.0;
                if (this.noteState == NoteState.kHasNote || this.noteState == NoteState.kAligning) {
                    this.feederState = FeederState.kIntake;
                }
                break;
            case kShoot:
                desiredRPM = Constants.FEEDER.FEEDER_SHOOT_RPM;
                this.noteState = NoteState.kNone;
                pidSlot = Constants.FEEDER.PID_SHOOT_SLOT;
                break;
            case kIntake:
                switch (noteState) {
                    case kHasNote: // Maybe want to lower speed when note passes the front beam break??
                    // Fall through intentional
                    case kNone:
                        desiredRPM = Constants.FEEDER.FEEDER_INTAKE_RPM;
                        break;
                    case kAligning:
                        desiredRPM = Constants.FEEDER.FEEDER_ALIGN_RPM;
                        break;
                    case kStaged:
                        desiredRPM = 0.0;
                        break;
                }
                break;
            case kOverride: // DesiredRPM is already set
                break;
            case kOutake:
                desiredRPM = Constants.FEEDER.FEEDER_OUTAKE_RPM;
                this.noteState = NoteState.kNone;
                break;
        }

        feederMotor.set(ControlType.kVelocity, desiredRPM, pidSlot);
    }
}