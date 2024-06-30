package lib.frc8592.hardware.motors;

import lib.frc8592.hardware.*;
import lib.frc8592.ProfileGains;
import com.revrobotics.*;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class VortexMotor extends Motor {
    private CANSparkFlex motor;
    private SparkPIDController motorCtrl;
    private RelativeEncoder motorEncoder;

    public VortexMotor(int id) {
        this(id, false);
    }

    public VortexMotor(int id, boolean reversed) {
        motor = new CANSparkFlex(id, MotorType.kBrushless);
        motor.setInverted(reversed);
        motorCtrl = motor.getPIDController();
        motorEncoder = motor.getEncoder();
    }

    @Override
    public int getMotorID() {
        return this.motor.getDeviceId();
    }

    @Override
    public void follow(Motor other, boolean reversed) {
        if (other.getClass() != VortexMotor.class) {
            throw new UnsupportedOperationException("Can only follow another Vortex motor");
        }
        this.motor.follow(((VortexMotor)other).motor, reversed);
    }

    @Override
    public void withGains(ProfileGains gains, int index) {
        HardwareUtils.setPIDGains(motor, gains.setSlot(index));
    }

    @Override
    public double getVelocity() {
        return motorEncoder.getVelocity();
    }

    @Override
    public double getPosition() {
        return motorEncoder.getPosition();
    }

    @Override
    public void setCurrent(int amps) {
        this.motor.setSmartCurrentLimit(amps);
        this.motor.setSecondaryCurrentLimit(amps);
    }

    @Override
    public void setVelocity(double velocityMetersPerSecond, int pidSlot) {
        motorCtrl.setReference(velocityMetersPerSecond, ControlType.kVelocity, pidSlot);
    }

    @Override
    public void setProfiledVelocity(double velocityMetersPerSecond, int pidSlot) {
        motorCtrl.setReference(velocityMetersPerSecond, ControlType.kSmartVelocity, pidSlot);
    }

    @Override
    public void setPosition(double positionRotations, int pidSlot) {
        motorCtrl.setReference(positionRotations, ControlType.kSmartMotion, pidSlot);
    }
}