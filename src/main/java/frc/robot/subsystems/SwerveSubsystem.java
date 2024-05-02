package frc.robot.subsystems;

import lib.frc8592.MatchMode;
import lib.frc8592.ProfileGains;
import lib.frc8592.swervelib.gyro.NewtonPigeon2;
import lib.frc8592.hardware.motors.FalconMotor;
import lib.frc8592.logging.SmartLogger;
import lib.frc8592.swervelib.NewtonSwerve;
import lib.frc8592.swervelib.sds.SDSMk4SwerveModule;
import lib.frc8592.swervelib.SwerveModule;
import lib.frc8592.swervelib.NewtonSwerve.SwerveProfile;
import lib.frc8592.swervelib.sds.SDSMk4SwerveModule.SDSConfig;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class SwerveSubsystem extends Subsystem {
    private static SwerveSubsystem INSTANCE = null;
    public static SwerveSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new SwerveSubsystem();
        return INSTANCE;
    }

    private NewtonSwerve swerve;
    private ChassisSpeeds desiredSpeeds;
    private DriveMode driveMode;

    private Command onTheFlyPathCommand;
    
    private ProfileGains kTurnToGains = new ProfileGains()
        .setP(0.05)
        .setD(0.001)
        .setMaxVelocity(3.0)
        .setMaxAccel(3.0)
        ;

    private enum DriveMode {
        kNormal,
        kLocked,
        kOnTheFlyPathFollower;
    }

    private SwerveSubsystem() {
        this.swerve = new NewtonSwerve(
            new SwerveProfile(
                0.8, 
                0.8, 
                4.5
            ),
            new NewtonPigeon2(0),
            new SwerveModule[] {
                new SDSMk4SwerveModule( // Front left
                    new FalconMotor(0), 
                    new FalconMotor(0), 
                    SDSConfig.Mk4L2, 
                    true
                ),
                new SDSMk4SwerveModule( // Front right
                    new FalconMotor(0), 
                    new FalconMotor(0), 
                    SDSConfig.Mk4L2, 
                    true
                ),
                new SDSMk4SwerveModule( // Back left
                    new FalconMotor(0), 
                    new FalconMotor(0), 
                    SDSConfig.Mk4L2, 
                    true
                ),
                new SDSMk4SwerveModule( // Back right
                    new FalconMotor(0), 
                    new FalconMotor(0), 
                    SDSConfig.Mk4L2, 
                    true
                ),
            }
        );

        super.logger = new SmartLogger("Swerve", true);
    }

    /**
     * The field-based absolute position of the robot
     */
    public Pose2d getCurrentPose() {
        if (Robot.isSimulation()) { // Use sim field position in simulation
            return Robot.FIELD.getRobotPose();
        }
        return this.swerve.getCurrentPose();
    }

    /**
     * The field-based absolute rotation of the robot
     */
    public Rotation2d getCurrentRotation() {
        if (Robot.isSimulation()) { // Use sim field rotation in simulation
            return Robot.FIELD.getRobotPose().getRotation();
        }
        return this.swerve.getYaw();
    }

    /**
     * The current translational and rotational speeds of the robot
     */
    public ChassisSpeeds getCurrentSpeeds() {
        if (Robot.isSimulation()) { // Desired speeds are theoretical speeds for simulation
            return this.desiredSpeeds;
        }
        return this.swerve.getChassisSpeeds();
    }

    /**
     * Sets the field-relative start position of the robot
     */
    public void setStartPose(Pose2d startPose) {
        this.swerve.resetPose(startPose);
        this.swerve.setYaw(startPose.getRotation().getDegrees());
    }

    /**
     * Resets the current yaw of the gyroscope as its 0 position
     */
    public void resetGyroscope() {
        this.swerve.setYaw(0.0);
    }

    /**
     * Sets the desired chassis translational and rotational speeds field relative
     */
    public void drive(ChassisSpeeds desiredSpeeds) {
        this.drive(desiredSpeeds, false);
    }

    /**
     * Turns to desired angle at current translational speeds
     */
    public void turnToAngle(double degrees) {
        this.desiredSpeeds = new ChassisSpeeds(
            desiredSpeeds.vxMetersPerSecond,
            desiredSpeeds.vyMetersPerSecond,
            kTurnToGains.toProfiledPIDController().calculate(
                this.swerve.getYaw().getDegrees(),
                degrees
            )
        );
    }

    /**
     * Drives to a particular point on the field at a specified end rotation
     */
    public void driveToPose(Pose2d targetPose) {
        if (targetPose == null) {
            if (onTheFlyPathCommand != null) {
                onTheFlyPathCommand.end(true);
                onTheFlyPathCommand = null;
            }
            return;
        }

        if (this.onTheFlyPathCommand == null) {
            this.onTheFlyPathCommand = AutoBuilder.pathfindToPoseFlipped(
                targetPose,
                new PathConstraints(4, 3, 4*Math.PI, 2*Math.PI)
            );
            this.onTheFlyPathCommand.initialize();
        }

        this.driveMode = DriveMode.kOnTheFlyPathFollower;
    }

    /**
     * Lock the wheel in an 'X' pattern
     */
    public void lockWheels() {
        this.driveMode = DriveMode.kLocked;
    }

    /**
     * Sets the desired chassis translational and rotational speeds robot or field relative
     */
    public void drive(ChassisSpeeds desiredSpeeds, boolean robotRelative) {
        if (robotRelative) {
            this.desiredSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(desiredSpeeds, this.swerve.getYaw());
        } else {
            this.desiredSpeeds = desiredSpeeds;
        }

        driveMode = DriveMode.kNormal;
    }

    @Override
    public void init(MatchMode mode) {
        driveMode = DriveMode.kNormal;
        desiredSpeeds = new ChassisSpeeds();
    }

    @Override
    public void initializeLogs() {
        this.logger.setEnum("Drive Mode", () -> driveMode);
        this.logger.setData("Current Pose", () -> getCurrentPose());
        this.logger.setData("Desired Speeds", () -> desiredSpeeds);
    }

    @Override
    public void periodic() {
        if (driveMode == null) return; // Do nothing if driveMode is null
        switch(driveMode) {
            case kNormal:
                this.swerve.drive(desiredSpeeds);
                break;
            case kLocked:
                this.swerve.lockWheels();
                break;
            case kOnTheFlyPathFollower:
                if (onTheFlyPathCommand != null) {
                    onTheFlyPathCommand.execute();
                    if (onTheFlyPathCommand.isFinished()) {
                        onTheFlyPathCommand.end(false);
                        onTheFlyPathCommand = null;
                        this.driveMode = DriveMode.kNormal;
                    }
                } else {
                    this.driveMode = DriveMode.kNormal;
                }
                break;
            default:
                // Do nothing
                break;
        }

        if (Robot.isSimulation() && Robot.MODE != MatchMode.AUTONOMOUS || Robot.MODE != MatchMode.DISABLED) { // Autonomous has its own simulation stuff
            Robot.FIELD.setRobotPose( // Translate the simulated robot pose to simulate teleop movement
                getCurrentPose().transformBy(
                    new Transform2d(
                        new Translation2d(
                            0.02 * desiredSpeeds.vxMetersPerSecond,
                            0.02 * desiredSpeeds.vyMetersPerSecond
                        ),
                        Rotation2d.fromRadians(0.02 * desiredSpeeds.omegaRadiansPerSecond)
                    )
                )
            );
        }
    }
}