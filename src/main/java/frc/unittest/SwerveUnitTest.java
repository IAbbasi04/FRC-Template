package frc.unittest;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveUnitTest extends UnitTest {
    private Timer timer = new Timer();

    @Override
    public void initialize() {
        SwerveSubsystem.getInstance().drive(new ChassisSpeeds()); // Reset drive speeds just in case
        timer.reset();
        timer.start();
    }

    @Override
    public void run() {
        if (timer.get() <= 3) {
            SwerveSubsystem.getInstance().drive(new ChassisSpeeds(2, 0, 0));
        } else if (timer.get() <= 6) {
            SwerveSubsystem.getInstance().drive(new ChassisSpeeds(0, 2, 0));
        } else {
            SwerveSubsystem.getInstance().drive(new ChassisSpeeds(0, 0, 2));
        }
    }

    @Override
    public boolean hasFailed() {
        return timer.get() >= 2;
    }

    @Override
    public boolean hasSucceeded() {
        return false;
    }

    @Override
    public boolean hasGivenUp() {
        return false;
    }

    @Override
    public String toString() {
        return "Swerve Directional Drive Test";
    }
}