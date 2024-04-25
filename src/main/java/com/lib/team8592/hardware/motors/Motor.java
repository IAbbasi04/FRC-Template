package com.lib.team8592.hardware.motors;

import com.lib.team8592.ProfileGains;

public abstract class Motor {
    /**
     * The output type (Velocity is in RPM, Position is in Rotations, Percent Output is [-100%, 100%], Voltage is in volts)
     */
    public enum ControlType {
        kVelocity,
        kPosition,
        kPercentOutput,
        kVoltage,
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
     * Sets the motor to the demand in the desired control mode
     */
    public void set(ControlType controlType, double demand) {
        set(controlType, demand, 0);
    }

    /**
     * Sets the motor to the demand in the desired control mode at a particular pid slot
     */
    public abstract void set(ControlType controlType, double demand, int pidSlot);
}