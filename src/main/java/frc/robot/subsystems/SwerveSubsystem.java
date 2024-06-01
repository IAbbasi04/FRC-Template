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
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;

public class SwerveSubsystem extends Subsystem {
    private static SwerveSubsystem INSTANCE = null;
    public static SwerveSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new SwerveSubsystem();
        return INSTANCE;
    }

    private NewtonSwerve swerve;
    private ChassisSpeeds desiredSpeeds = new ChassisSpeeds();
    private DriveMode driveMode = DriveMode.kNormal;
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

        super.logger = new SmartLogger("SwerveSubsystem");
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
        // Desired speeds are theoretical speeds for simulation
        if (Robot.isSimulation()) return this.desiredSpeeds;
        return this.swerve.getChassisSpeeds();
    }

    /**
     * Sets the field-relative start position of the robot
     */
    public void setStartPose(Pose2d startPose) {
        this.swerve.resetPose(startPose);
        this.swerve.setYaw(startPose.getRotation().getDegrees());

        if (Robot.isReal()) return;
        Robot.FIELD.setRobotPose(startPose);
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
        this.logger.logEnum("Drive Mode", () -> driveMode);
        this.logger.logData("Current Pose", () -> getCurrentPose());
        this.logger.logData("Desired Speeds", () -> desiredSpeeds);
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
                // Add code to run a path following command
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