package frc.robot.common.crescendo;

public class ShotProfile {
    public double leftFlywheelRPM = 0.0;
    public double rightFlywheelRPM = 0.0;
    public double elevatorPivotDegrees = 0.0;
    public double elevatorExtensionMeters = 0.0;

    public boolean shouldShoot = true;

    public ShotProfile() {

    }

    public ShotProfile flywheel(double left, double right) {
        this.leftFlywheelRPM = left;
        this.rightFlywheelRPM = right;
        return this;
    }

    public ShotProfile pivot(double angle) {
        this.elevatorPivotDegrees = angle;
        return this;
    }

    public ShotProfile extension(double extension) {
        this.elevatorExtensionMeters = extension;
        return this;
    }

    public ShotProfile shouldShoot(boolean shouldShoot) {
        this.shouldShoot = shouldShoot;
        return this;
    }
}