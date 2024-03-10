package frc.robot.autonomous.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.modules.DriveModule;

public class DeliverCommand extends Command {
    private DeliverType type;
    private boolean useVision = false;
    private Pose2d target;

    public enum DeliverType {
        AMP,
        SPEAKER,
        GROUND;
    }

    public DeliverCommand(DeliverType type) {
        this.type = type;
    }

    /**
     * Allows vision for shooter and pivot locking
     */
    public DeliverCommand addVision() {
        useVision = true;
        return this;
    }

    /**
     * Allows vision for shooter and pivot locking
     */
    public DeliverCommand addPoseTarget(Pose2d target) {
        this.target = target;
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            this.target = new Pose2d(
                Constants.FIELD.RED_WALL_X - target.getX(),
                target.getY(),
                Rotation2d.fromDegrees(target.getRotation().getDegrees())
            );
        }
        return this;
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("DELIVER MODE", type.toString());
        SmartDashboard.putBoolean("VISION LOCK", useVision);
        if (target != null) {
            Robot.FIELD.getObject("TARGET").setPose(target);
        }
    }

    @Override
    public boolean execute() {
        if (target != null) {
            Pose2d currentPose = DriveModule.getInstance().getCurrentPose();
            double dx = currentPose.getX() - target.getX();
            double dy = currentPose.getY() - target.getY();
            Rotation2d desiredAngle = Rotation2d.fromRadians(Math.PI + Math.atan(dy/dx));
            Robot.FIELD.setRobotPose(new Pose2d(currentPose.getTranslation(), desiredAngle));
        }

        if (commandTimer.get() >= startDelay) {
            return commandTimer.get() >= 0.75;
        }
        return false;
    }

    @Override
    public void shutdown() {
        SmartDashboard.putString("DELIVER MODE", "N/A");
        SmartDashboard.putBoolean("VISION LOCK", false);
        // Robot.FIELD.getObject("TARGET").close();
    }
    
}