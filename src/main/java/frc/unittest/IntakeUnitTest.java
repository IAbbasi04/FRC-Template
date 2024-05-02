package frc.unittest;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;

public class IntakeUnitTest extends UnitTest {
    @Override
    public void initialize() {
        SwerveSubsystem.getInstance().drive(new ChassisSpeeds()); // Reset drive speeds just in case
    }

    @Override
    public void run() {
        SmartDashboard.putNumber("TIME", testTimer.get());
    }

    @Override
    public boolean hasFailed() {
        return false;
    }

    @Override
    public boolean hasSucceeded() {
        return testTimer.get() >= 3;
    }
}
