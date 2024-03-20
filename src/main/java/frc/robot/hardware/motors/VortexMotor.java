package frc.robot.hardware.motors;

import com.revrobotics.*;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.hardware.HardwareUtils;
import frc.robot.common.ProfileGains;

public class VortexMotor extends Motor {
    private CANSparkFlex motor;
    private SparkPIDController motorCtrl;
    private RelativeEncoder motorEncoder;

    /**
     * Creates a neo motor with a particular CAN ID
     */
    public VortexMotor(int id) {
        this(id, false);
    }

    /**
     * Creates a neo motor with a particular CAN ID
     */
    public VortexMotor(int id, boolean reversed) {
        motor = new CANSparkFlex(id, MotorType.kBrushless);
        motor.setInverted(reversed);
        motorCtrl = motor.getPIDController();
        motorEncoder = motor.getEncoder();
    }

    @Override
    public void withGains(ProfileGains gains, int index) {
        HardwareUtils.setPIDGains(motor, gains);
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
    public void set(ControlType controlType, double demand, int pidSlot) {
        switch(controlType) {
            case kPercentOutput:
                motor.set(demand);
                break;
            case kVelocity:
                motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kSmartVelocity, pidSlot);
                break;
            case kPosition:
                motorCtrl.setReference(demand, com.revrobotics.CANSparkBase.ControlType.kSmartMotion, pidSlot);
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