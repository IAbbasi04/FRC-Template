package frc.robot.modules;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.Constants;
import frc.robot.common.Ports;
import frc.robot.common.ProfileGains;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.BeamSensor;
import frc.robot.hardware.motors.VortexMotor;
import frc.robot.hardware.motors.Motor.ControlType;

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
        kOverride,
        kShoot,
        kIntake,
        kOff,
        kOutake,
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
    }

    /**
     * Sets the RPM demand of the feeder motor
     */
    public void setFeederVelocity(double rpm) {
        this.desiredRPM = rpm;
        this.feederState = FeederState.kOverride;
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

    @Override
    public void init(MatchMode mode) {
        feederMotor.set(ControlType.kVelocity, 0, Constants.FEEDER.PID_FEED_SLOT);
    }

    @Override
    public void periodic() {
        if (exitBeam.isBroken()) { // Any point the exit beam is broken align the note with the entry beam
            noteState = NoteState.kAligning;
        } else if (entryBeam.isBroken()) {
            if (noteState.equals(NoteState.kNone)) { // Note not previously in robot
                this.hasNote = true;
            } else if (noteState.equals(NoteState.kAligning)) { // Align until the note hits the entry beam sensor
                noteState = NoteState.kStaged;
            }
        }

        switch (feederState) {
            case kOff:
                feederMotor.set(ControlType.kVelocity, 0);
                if (this.noteState == NoteState.kHasNote) {
                    this.feederState = FeederState.kIntake;
                }
                break;
            case kShoot:
                feederMotor.set(ControlType.kVelocity, Constants.FEEDER.FEEDER_SHOOT_RPM);
                break;
            case kIntake:
                switch (noteState) {
                    case kHasNote: // Maybe want to lower speed when note passes the front beam break??
                    // Fall through intentional
                    case kNone:
                        feederMotor.set(ControlType.kVelocity, Constants.FEEDER.FEEDER_INTAKE_RPM);
                        break;
                    case kAligning:
                        feederMotor.set(ControlType.kVelocity, Constants.FEEDER.FEEDER_ALIGN_RPM);
                        break;
                    case kStaged:
                        feederMotor.set(ControlType.kVelocity, 0.0);
                        break;
                }
                break;
            case kOverride:
                feederMotor.set(ControlType.kVelocity, desiredRPM);
                break;
            case kOutake:
                feederMotor.set(ControlType.kVelocity, Constants.FEEDER.FEEDER_OUTAKE_RPM);
                break;
        }

        SmartDashboard.putString("FEEDER/FEEDER STATE", feederState.name());
        SmartDashboard.putString("FEEDER/NOTE STATE", noteState.name());

        SmartDashboard.putBoolean("FEEDER/ENTRY BEAM", entryBeam.isBroken());
        SmartDashboard.putBoolean("FEEDER/EXIT BEAM", exitBeam.isBroken());
    }
}