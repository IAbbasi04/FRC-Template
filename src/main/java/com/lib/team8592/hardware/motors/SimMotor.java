package com.lib.team8592.hardware.motors;

import com.lib.team8592.ProfileGains;

/**
 * - Class meant to easily simulate a motor in the gradle sim
 * - Currently having trouble with actual motor output values
 * - Meant to share methods and structure with actual motors 
 *   such that it can be easily replaced with a single line
 */
public class SimMotor extends Motor {
    private double currentVelocity;
    private double currentPosition;
    
    private double desiredVelocity;
    private double desiredPosition;

    private int id = -1;

    private ProfileGains velocityGains = new ProfileGains()
        .setP(0.25)
        .setI(0.00)
        .setD(0.05)
        .setFF(0.10)
        .setMaxVelocity(200) // RPM
        .setMaxAccel(500) // RPM per second
    ;

    private ProfileGains positionGains = new ProfileGains()
        .setP(0.25)
        .setI(0.00)
        .setD(0.05)
        .setMaxVelocity(200) // RPM
        .setMaxAccel(500) // RPM per second
    ;

    public SimMotor(int id) {
        this(id, false);
    }

    public SimMotor(int id, boolean reversed) {
        // Ignore reversing since it does not matter for simulation
        this.id = id;
    }

    @Override
    public int getMotorID() {
        return this.id;
    }

    @Override
    public void follow(Motor other, boolean reversed) {
        // Current not doing anything since it is simulation
        // TODO - Actually simulating following
    }

    @Override
    public void withGains(ProfileGains gains, int index) {
        // Since this is simulated, we ignore any other PID gains
        // TODO - Maybe add ability to??
    }

    /**
     * Position setpoint for the simulated motor
     */
    public double getDesiredPosition() {
        return this.desiredPosition;
    }

    /**
     * Velocity setpoint for the simulated motor
     */
    public double getDesiredVelocity() {
        return this.desiredVelocity;
    }

    @Override
    public double getVelocity() {
        return this.currentVelocity;
    }

    @Override
    public double getPosition() {
        return this.currentPosition;
    }

    @Override
    public void set(ControlType controlType, double demand, int pidSlot) {
        switch (controlType) {
            case kPercentOutput:
                desiredVelocity = demand;
                currentVelocity = demand;
                break;
            case kVoltage:
                desiredVelocity = demand;
                currentVelocity = demand;
                break;
            case kPosition:
                desiredPosition = demand;
                currentPosition += positionGains.toProfiledPIDController().calculate(currentPosition, desiredPosition);
                break;
            case kVelocity:
                desiredVelocity = demand;
                currentVelocity += velocityGains.toProfiledPIDController().calculate(currentVelocity, desiredVelocity);
                break;
        }
    }
}