package com.lib.team8592.hardware.motors;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.controls.*;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.lib.team8592.ProfileGains;

public class FalconMotor extends Motor {
    private TalonFX falconMotor;
    private TalonFXConfiguration configuration;

    private PositionVoltage positionOutput;
    private VelocityVoltage velocityOutput;

    public FalconMotor(int id) {
        this(id, false);
    }

    public FalconMotor(int id, boolean reversed) {
        this.falconMotor = new TalonFX(id);
        this.falconMotor.setInverted(reversed);

        this.configuration = new TalonFXConfiguration();
        this.configuration.MotorOutput.Inverted = reversed ? 
            InvertedValue.Clockwise_Positive :
            InvertedValue.CounterClockwise_Positive;

        this.falconMotor.getConfigurator().apply(configuration);

        this.positionOutput = new PositionVoltage(0.0);
        this.velocityOutput = new VelocityVoltage(0.0);
    }

    @Override
    public int getMotorID() {
        return this.falconMotor.getDeviceID();
    }

    @Override
    public void follow(Motor other, boolean reversed) {
        this.falconMotor.setControl(new Follower(other.getMotorID(), reversed));
    }

    @Override
    public void withGains(ProfileGains gains, int index) {
        switch (index) {
            case 0:
                Slot0Configs slot0Config = new Slot0Configs()
                    .withKP(gains.getP())
                    .withKI(gains.getI())
                    .withKD(gains.getD());

                this.falconMotor.getConfigurator().apply(slot0Config);
            case 1:
                Slot1Configs slot1Config = new Slot1Configs()
                    .withKP(gains.getP())
                    .withKI(gains.getI())
                    .withKD(gains.getD());

                this.falconMotor.getConfigurator().apply(slot1Config);
                break;
            case 2:
                Slot2Configs slot2Config = new Slot2Configs()
                    .withKP(gains.getP())
                    .withKI(gains.getI())
                    .withKD(gains.getD());

                this.falconMotor.getConfigurator().apply(slot2Config);
                break;
            default:
                SlotConfigs slotConfig = new SlotConfigs()
                    .withKP(gains.getP())
                    .withKI(gains.getI())
                    .withKD(gains.getD());

                this.falconMotor.getConfigurator().apply(slotConfig);
                break;
        }
    }

    @Override
    public double getVelocity() {
        return this.falconMotor.getVelocity().getValueAsDouble();
    }

    @Override
    public double getPosition() {
        return this.falconMotor.getPosition().getValueAsDouble();
    }

    @Override
    public void set(ControlType controlType, double demand, int pidSlot) {
        switch (controlType) {
            case kPosition:
                falconMotor.setControl(positionOutput.withPosition(demand));
                break;
            case kVelocity:
                falconMotor.setControl(velocityOutput.withVelocity(demand).withAcceleration(1.0));
                break;
            case kPercentOutput:
                falconMotor.set(demand);
                falconMotor.setPosition(demand);
                break;
            case kVoltage:
                falconMotor.setVoltage(demand);
                break;
            default:
                break;
        }
    }
}