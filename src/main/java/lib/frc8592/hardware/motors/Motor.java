package lib.frc8592.hardware.motors;

import lib.frc8592.ProfileGains;

public abstract class Motor {
    /**
     * The CAN ID of the motor
     */
    public abstract int getMotorID();

    /**
     * Sets the motor following another either directly or reversed
     */
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
     * Sets the motor to the desired velocity in meters per second
     */
    public void setVelocity(double velocityMetersPerSecond) {
        setVelocity(velocityMetersPerSecond, 0);
    }

    /**
     * Sets the motor to the desired velocity in meters per second
     */
    public void setProfiledVelocity(double velocityMetersPerSecond) {
        setProfiledVelocity(velocityMetersPerSecond, 0);
    }

    /**
     * Sets the motor to the desired position in rotations
     */
    public void setPosition(double positionRotations) {
        setPosition(positionRotations, 0);
    }

    /**
     * Sets the motor to the desired velocity in meters per second
     */
    public abstract void setVelocity(double velocityMetersPerSecond, int pidSlot);

    /**
     * Sets the motor to the desired velocity in meters per second using a trapezoidal profile
     */
    public abstract void setProfiledVelocity(double velocityMetersPerSecond, int pidSlot);

    /**
     * Sets the motor to the desired position in rotations
     */
    public abstract void setPosition(double positionRotations, int pidSlot);
}