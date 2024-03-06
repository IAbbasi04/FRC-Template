package frc.robot.autonomous;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.Trajectory.State;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Robot;
import frc.robot.common.ProfileGains;
import frc.robot.modules.DriveModule;

public class SwerveTrajectory {
    private Trajectory trajectory;
    private Rotation2d endRotation;
    private double turnDelay = 0.0; // seconds

    private HolonomicDriveController drivePID;

    private ProfileGains translateGains = new ProfileGains()
        .setP(3.0)
        .setTolerance(Units.inchesToMeters(1)); // meters

    private ProfileGains rotateGains = new ProfileGains()
        .setP(0.04)
        .setI(0)
        .setD(0)
        .setMaxVelocity(4 * Math.PI)
        .setMaxAccel(2 * Math.PI)
        .setContinuousInput(-Math.PI, Math.PI)
        .setTolerance(3); // degrees

    public SwerveTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
        
        drivePID = new HolonomicDriveController(
            translateGains.toPIDController(), 
            translateGains.toPIDController(), 
            rotateGains.toProfiledPIDController()
        );

        drivePID.setTolerance(
            new Pose2d(
                translateGains.getTolerance(), 
                translateGains.getTolerance(),
                Rotation2d.fromDegrees(rotateGains.getTolerance())
            )
        );

        // if (DriverStation.getAlliance().g) {

        // }
        addRotation(new Rotation2d());
    }

    /**
     * Sets the ending rotation of the robot after the path ends
     */
    public SwerveTrajectory addRotation(Rotation2d endRotation, double turnDelay) {
        this.turnDelay = turnDelay;
        this.endRotation = endRotation;
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            this.endRotation = Rotation2d.fromDegrees(180 - endRotation.getDegrees());
        }
        return this;
    }

    /**
     * Sets the ending rotation of the robot after the path ends
     */
    public SwerveTrajectory addRotation(Rotation2d endRotation) {
        return addRotation(endRotation, 0.0);
    }

    /**
     * Gets the position of the robot at the start of the path
     */
    public Pose2d getInitialPose() {
        return trajectory.sample(0.0).poseMeters;
    }

    /**
     * Whether the path has been completed
     */
    public boolean isFinished(double time) {
        return time >= trajectory.getTotalTimeSeconds();
    }

    /**
     * Simulates the position of the robot
     */
    private void simulate(Pose2d pose) {
        Robot.FIELD.setRobotPose(pose);
    }

    /**
     * The desired speed of the robot at a given time within a trajectory
     */
    public ChassisSpeeds sample(double time) {
        Pose2d currentPose = DriveModule.getInstance().getCurrentPose();
        State desiredState = trajectory.sample(time);
        ChassisSpeeds desiredSpeeds = drivePID.calculate(currentPose, desiredState, endRotation);

        if (time < turnDelay) {
            desiredSpeeds = new ChassisSpeeds(
                desiredSpeeds.vxMetersPerSecond,
                desiredSpeeds.vyMetersPerSecond,
                0.0
            );
        }

        if (!Robot.isReal()) {
            simulate(new Pose2d(desiredState.poseMeters.getTranslation(), endRotation));
        }

        return desiredSpeeds;
    }
}