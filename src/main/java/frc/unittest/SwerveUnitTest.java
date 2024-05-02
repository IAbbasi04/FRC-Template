package frc.unittest;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveUnitTest extends UnitTest {
    @Override
    public void initialize() {
        SwerveSubsystem.getInstance().drive(new ChassisSpeeds()); // Reset drive speeds just in case
    }

    @Override
    public void run() {
        if (testTimer.get() <= 3) {
            SwerveSubsystem.getInstance().drive(new ChassisSpeeds(2, 0, 0));
        } else if (testTimer.get() <= 6) {
            SwerveSubsystem.getInstance().drive(new ChassisSpeeds(0, 2, 0));
        } else {
            SwerveSubsystem.getInstance().drive(new ChassisSpeeds(0, 0, 2));
        }

        SmartDashboard.putNumber("TIME", testTimer.get());
    }

    @Override
    public boolean hasFailed() {
        return testTimer.get() >= 3;
    }

    @Override
    public boolean hasSucceeded() {
        return false;
    }
}