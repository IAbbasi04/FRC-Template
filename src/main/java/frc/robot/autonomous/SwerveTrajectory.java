package frc.robot.autonomous;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
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
    private Rotation2d startRotation;
    private double turnDelay = 0.0; // seconds

    private HolonomicDriveController drivePID;

    private ProfileGains translateGains = new ProfileGains()
        .setP(6.0)
        .setTolerance(Units.inchesToMeters(1)); // meters

    private ProfileGains rotateGains = new ProfileGains()
        .setP(3.0)
        .setI(0)
        .setD(0)
        .setMaxVelocity(360)
        .setMaxAccel(360)
        .setContinuousInput(0, 360)
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

        addRotation(new Rotation2d());
        startRotation = new Rotation2d();
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            startRotation = Rotation2d.fromDegrees(180 - startRotation.getDegrees());
        }
    }

    /**
     * Sets the starting rotation of the trajectory
     */
    public SwerveTrajectory setStartAngle(Rotation2d startAngle) {
        this.startRotation = startAngle;
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            startRotation = Rotation2d.fromDegrees(180 - startRotation.getDegrees());
        }
        return this;
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
        return new Pose2d(
            trajectory.sample(0.0).poseMeters.getTranslation(),
            startRotation
        );
        // return trajectory.sample(0.0).poseMeters;
    }

    /**
     * Whether the path has been completed
     */
    public boolean isFinished(double time) {
        return time >= trajectory.getTotalTimeSeconds() * 1.2 ||
        (drivePID.atReference() && time >= trajectory.getTotalTimeSeconds());
    }

    /**
     * Simulates the position of the robot on the field
     */
    public void simulate(ChassisSpeeds speeds, boolean simTrajectory) {
        Pose2d robotPose = Robot.FIELD.getRobotPose();

        Pose2d newPose = new Pose2d(
            new Translation2d(
                robotPose.getX() - 0.1 * speeds.vxMetersPerSecond,
                robotPose.getY() - 0.1 * speeds.vyMetersPerSecond
            ),
            robotPose.getRotation().plus(Rotation2d.fromRadians(0.02 * speeds.omegaRadiansPerSecond))
        );

        if (simTrajectory) {
            Robot.FIELD.getObject("TRAJECTORY").setTrajectory(trajectory);
        }

        Robot.FIELD.setRobotPose(newPose);
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
            simulate(desiredSpeeds, false);
        }

        return desiredSpeeds;
    }
}