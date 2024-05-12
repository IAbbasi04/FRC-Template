package lib.frc8592.hardware.motors;

import lib.frc8592.ProfileGains;

public abstract class Motor {
    /**
     * The output type
     */
    public enum ControlType {
        kVelocity, // RPM
        kPosition, // Rotations
        kPercentOutput, // [-1, 1]
        kVoltage, // Volts
    }

    public abstract int getMotorID();

    public abstract void follow(Motor other, boolean reversed);

    /**
     * Sets the PID gains for a particular motor slot
     */
    public abstract void withGains(ProfileGains gains, int index);
    
    /**
     * Returns the velocity of the motor in RPM
     */
    public abstract double getVelocity();

    /**
     * Returns the position of the motor in Rotations
     */
    public abstract double getPosition();

    /**
     * Sets max allowed current in amps
     */
    public abstract void setCurrent(int amps);

    /**
     * Sets the motor to the demand in the desired control mode
     */
    public void set(ControlType controlType, double demand) {
        set(controlType, demand, 0);
    }

    /**
     * Sets the motor to the demand in the desired control mode at a particular pid slot
     */
    public abstract void set(ControlType controlType, double demand, int pidSlot);

    /**
     * Returns this as a vortex motor
     */
    public VortexMotor asVortex() {
        return (VortexMotor)this;
    }

    /**
     * Returns this as a falcon motor
     */
    public FalconMotor asFalcon() {
        return (FalconMotor)this;
    }
}