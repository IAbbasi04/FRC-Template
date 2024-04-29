package lib.frc8592.swervelib.gyro;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

public class NewtonPigeon2 implements Gyro {
    private Pigeon2 pigeon;
    private Pigeon2Configuration config;

    public NewtonPigeon2(int id) {
        this.pigeon = new Pigeon2(id);
        this.config = new Pigeon2Configuration();
        this.pigeon.getConfigurator().apply(config);
    }

    public double getYaw() {
        return this.pigeon.getYaw().getValueAsDouble() % 360.0;
    }

    public double getRoll() {
        return this.pigeon.getRoll().getValueAsDouble();
    }

    public double getPitch() {
        return this.pigeon.getPitch().getValueAsDouble();
    }

    public void zeroYaw() {
        this.pigeon.setYaw(0);
    }
    
    public void setYaw(double yaw){
        this.pigeon.setYaw(yaw);
    }

}