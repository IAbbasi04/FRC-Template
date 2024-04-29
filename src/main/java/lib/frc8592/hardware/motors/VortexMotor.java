package lib.frc8592.hardware.motors;

import lib.frc8592.hardware.*;
import lib.frc8592.ProfileGains;
import com.revrobotics.*;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class VortexMotor extends Motor {
    private CANSparkFlex motor;
    private SparkPIDController motorCtrl;
    private RelativeEncoder motorEncoder;
    private boolean useSmartMotion = false;

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
        useSmartMotion = gains.isSmartMotion();
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
    public void set(ControlType controlType, double demand, int pidSlot) {
        switch(controlType) {
            case kPercentOutput:
                motor.set(demand);
                break;
            case kVelocity:
                if (useSmartMotion) {
                    motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kSmartVelocity, pidSlot);
                } else {
                    motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kVelocity, pidSlot);
                }
                break;
            case kPosition:
                if (useSmartMotion) {
                    motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kSmartMotion, pidSlot);
                } else {
                    motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kPosition, pidSlot);
                }
                break;
            case kVoltage:
                motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kVoltage, pidSlot);
                break;
            default:
                motor.set(0.0);
                break;
        }
    }

    @Override
    public void set(ControlType controlType, double demand) {
        set(controlType, demand, 0);
    }
}