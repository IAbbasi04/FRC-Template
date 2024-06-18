package lib.frc8592.swervelib;

import lib.frc8592.hardware.motors.Motor;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public abstract class SwerveModule {
    protected Motor driveMotor, steerMotor;
    protected ModuleConfiguration moduleConfig;
    protected SwerveModuleState swerveModuleState = new SwerveModuleState();
    protected double wheelDiameterInches;

    /**
     * Sets the velocity of the drive motor in meters per second
     */
    protected void setDriveVelocity(double velocityMetersPerSecond) {
        double rps = velocityMetersPerSecond / 
            (moduleConfig.getDriveReduction() * Math.PI * moduleConfig.getWheelDiameter());
        driveMotor.setVelocity(rps);
    }

    /**
     * Sets the angle of the steer motor in degrees [-180, 180]
     */
    protected void setSteerAngle(double angleRadians) {
        double rotations = angleRadians / 
            (2 * Math.PI * moduleConfig.getSteerReduction());
        steerMotor.setPosition(rotations);
    }

    /**
     * The current velocity of the drive motor in meters per second
     */
    public double getDriveVelocity() {
        return driveMotor.getVelocity() * moduleConfig.getWheelDiameter() * Math.PI * moduleConfig.getDriveReduction();
    }

    /**
     * The current distance of the drive motor in meters
     */
    public double getDriveDistance() {
        return driveMotor.getPosition() * moduleConfig.getWheelDiameter() * Math.PI * moduleConfig.getDriveReduction();
    }

    /**
     * The current angle of the steer motor in degrees
     */
    public double getSteerAngle() {
        double rotations = steerMotor.getPosition() * moduleConfig.getSteerReduction();
        double degrees = rotations * 360.0;
        degrees = (degrees % 360) / 2.0; // Clamp between [-180, 180] degrees
        return degrees;
    }

    /**
     * Sets the desired state of the swerve module (steer angle, drive velocity)
     */
    public void setModuleState(SwerveModuleState moduleState) {
        swerveModuleState = moduleState;
        this.set(swerveModuleState.speedMetersPerSecond, swerveModuleState.angle.getRadians());
    }

    /**
     * The desired state of the swerve module
     */
    public SwerveModuleState getModuleState() {
        return swerveModuleState;
    }

    /**
     * Position of the steer angle and throttle
     */
    public SwerveModulePosition getModulePosition() {
        return new SwerveModulePosition(getDriveDistance(), Rotation2d.fromDegrees(getSteerAngle()));
    }

    /**
     * Sets max allowed current (in amps) for throttle motor
     */
    public void setMaxThrottleCurrent(int amps) {
        this.driveMotor.setCurrent(amps);
    }

    /**
     * Sets max allowed current (in amps) for throttle motor
     */
    public void setMaxSteerCurrent(int amps) {
        this.steerMotor.setCurrent(amps);
    }

    /**
     * Applies desired throttle velocity and steer angle 
     */
    public void set(double driveMetersPerSecond, double steerRadians) {
        steerRadians %= 2 * Math.PI; // Clamp within [-360, 360]
        if (steerRadians < 0.0) { // Convert to [0, 360]
            steerRadians += 2.0 * Math.PI;
        }

        double difference = steerRadians - getSteerAngle();
        // Change the target angle so the difference is in the range [-pi, pi) instead
        // of [0, 2pi)
        if (difference >= Math.PI) {
            steerRadians -= 2.0 * Math.PI;
        } else if (difference < -Math.PI) {
            steerRadians += 2.0 * Math.PI;
        }
        difference = steerRadians - getSteerAngle(); // Recalculate difference

        // If the difference is greater than 90 deg or less than -90 deg the drive can
        // be inverted so the total
        // movement of the module is less than 90 deg
        if (difference > Math.PI / 2.0 || difference < -Math.PI / 2.0) {
            // Only need to add 180 deg here because the target angle will be put back into
            // the range [0, 2pi)
            steerRadians += Math.PI;
            driveMetersPerSecond *= -1.0;
        }

        // Put the target angle back into the range [0, 2pi)
        steerRadians %= (2.0 * Math.PI);
        if (steerRadians < 0.0) {
            steerRadians += 2.0 * Math.PI;
        }

        this.setDriveVelocity(driveMetersPerSecond);
        this.setSteerAngle(steerRadians);
    }

    /**
     * Applies desired throttle velocity and steer angle 
     */
    public void set(SwerveModuleState state) {
        this.set(state.speedMetersPerSecond, state.angle.getRadians());
    }
}