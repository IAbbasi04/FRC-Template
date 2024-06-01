package lib.frc8592.swervelib.gyro;

public interface Gyro {
    public double getYaw();

    public double getRoll();

    public double getPitch();

    public void setYaw(double yaw);

    public void zeroYaw();

    // public boolean isRotating();

}
