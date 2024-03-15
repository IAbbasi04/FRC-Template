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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.common.ProfileGains;
import frc.robot.common.Utils;
import frc.robot.modules.DriveModule;

public class SwerveTrajectory {
    private Trajectory trajectory;
    private Rotation2d startRotation;

    private HolonomicDriveController drivePID;
    private RotationProfile rotationProfile;

    private VisionType visionType;

    private ProfileGains translateGains = new ProfileGains()
        .setP(6.0)
        .setTolerance(Units.inchesToMeters(1)); // meters

    private ProfileGains rotateGains = new ProfileGains()
        .setP(3.0)
        .setI(0)
        .setD(0)
        .setContinuousInput(0, 360)
        .setTolerance(3); // degrees

    private ProfileGains simRotateGains = new ProfileGains()
        .setP(0.1)
        .setI(0)
        .setD(0)
        .setContinuousInput(0, 360)
        .setTolerance(3); // degrees

    public static class RotationProfile {
        public Rotation2d rotation;
        public double maxSpeed;
        public double turnDelay;

        public RotationProfile(Rotation2d rotation, double maxSpeed, double turnDelay) {
            this.rotation = rotation;
            this.maxSpeed = maxSpeed;
            this.turnDelay = turnDelay;
            flipForRed();
        }

        public RotationProfile(Rotation2d rotation, double turnDelay) {
            this(rotation, Double.POSITIVE_INFINITY, turnDelay);
        }

        public RotationProfile(Rotation2d rotation) {
            this(rotation, 0.0);
        }

        public RotationProfile() {
            this(new Rotation2d());
        }

        public RotationProfile rotation(Rotation2d rotation) {
            this.rotation = rotation;
            return flipForRed();
        }

        public RotationProfile maxSpeed(double maxSpeed) {
            this.maxSpeed = maxSpeed;
            return this;
        }

        public RotationProfile delay(double turnDelay) {
            this.turnDelay = turnDelay;
            return this;
        }

        private RotationProfile flipForRed() {
            if (DriverStation.getAlliance().get() == Alliance.Red) {
                rotation = Rotation2d.fromDegrees(180 - rotation.getDegrees());
            }
            return this;
        }
    }

    public enum VisionType {
        kNone, // No targetting
        kStrafe, // Horizontal strafe lock
        kRotate, // Rotational lock
        kFetch // Drive to and rotate
    }

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

        // addRotation(new Rotation2d());
        startRotation = new Rotation2d();
        if (DriverStation.getAlliance().get() == Alliance.Red) {
            startRotation = Rotation2d.fromDegrees(180 - startRotation.getDegrees());
        }

        this.withRotation(new RotationProfile());
        visionType = VisionType.kNone;
    }

    /**
     * Adds a desired end rotation with an indicated delay and turn speed to the path
     */
    public SwerveTrajectory withRotation(RotationProfile rotationProfile) {
        this.rotationProfile = rotationProfile;
        this.rotateGains.setMaxVelocity(rotationProfile.maxSpeed);
        return this;
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
     * Adds vision for targetting in the trajectory
     */
    public SwerveTrajectory addVision(VisionType visionType) {
        this.visionType = visionType;
        return this;
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
        return /*time >= trajectory.getTotalTimeSeconds() * 1.2 ||*/
        (drivePID.atReference() && time >= trajectory.getTotalTimeSeconds());
    }

    /**
     * Simulates the position of the robot on the field
     */
    public void simulate(double time, boolean simTrajectory) {
        Pose2d robotPose = Robot.FIELD.getRobotPose();
        double turnSpeed = 
            simRotateGains.toPIDController().calculate(
                robotPose.getRotation().getDegrees(),
                rotationProfile.rotation.getDegrees()
            );

        if (time < rotationProfile.turnDelay) {
            turnSpeed = 0.0;
        }

        Rotation2d newRotation = robotPose.getRotation().plus(
            Rotation2d.fromDegrees(Utils.clamp(turnSpeed, rotationProfile.maxSpeed))
        );

        Pose2d newPose = new Pose2d(
            trajectory.sample(time).poseMeters.getTranslation(),
            newRotation
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
        ChassisSpeeds desiredSpeeds = drivePID.calculate(currentPose, desiredState, rotationProfile.rotation);

        double turnSpeed = desiredSpeeds.omegaRadiansPerSecond;
        if (time < rotationProfile.turnDelay) {
            turnSpeed = 0.0;
        }

        String lockString = "NOT LOCKING";
        switch(visionType) {
            case kStrafe:
                lockString = "STRAFING";
                break;
            case kRotate:
                lockString = "ROTATING";
            case kFetch:
                lockString += " AND DRIVING TOWARDS";
            default:
                break;
        }
        
        SmartDashboard.putString("FOLLOWER VISION", lockString);

        desiredSpeeds = new ChassisSpeeds(
            desiredSpeeds.vxMetersPerSecond,
            desiredSpeeds.vyMetersPerSecond,
            Utils.clamp(turnSpeed, rotationProfile.maxSpeed)
        );

        if (!Robot.isReal()) {
            simulate(time, false);
        }

        return desiredSpeeds;
    }
}