package lib.frc8592.swervelib;

import edu.wpi.first.math.estimator.*;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.Timer;
import lib.frc8592.swervelib.gyro.Gyro;

public class NewtonSwerve {
    private SwerveModule[] modules;
    private ChassisState currentState;
    private SwerveDriveOdometry odometry;
    private SwerveDriveKinematics kinematics;
    private Gyro gyro;
    private SwerveProfile profile;
    private SwerveDrivePoseEstimator poseEstimator;

    public NewtonSwerve(SwerveProfile profile, Gyro gyro, SwerveModule[] modules) {
        this.modules = modules;
        this.profile = profile;
        this.gyro = gyro;
        this.kinematics = new SwerveDriveKinematics(
            new Translation2d[]{
                new Translation2d(-profile.width / 2.0, profile.length / 2.0), // Front left
                new Translation2d(profile.width / 2.0, profile.length / 2.0), // Front right
                new Translation2d(-profile.width / 2.0, -profile.length / 2.0), // Back left
                new Translation2d(profile.width / 2.0, -profile.length / 2.0) // Back right
            }
        );

        SwerveModulePosition[] initialPositions = new SwerveModulePosition[] { new SwerveModulePosition(), new SwerveModulePosition(),
                        new SwerveModulePosition(), new SwerveModulePosition() };

        this.odometry = new SwerveDriveOdometry(kinematics, new Rotation2d(), initialPositions);
        this.poseEstimator = new SwerveDrivePoseEstimator(kinematics, getYaw(), initialPositions, getCurrentPose());

        this.setMaxThrottleCurrent(80); // Defaults to 80 amps
        this.setMaxSteerCurrent(20); // Defaults to 20 amps
    }

    /**
     *  Use vision data to adjust odometry
     */
    public void addVisionPoseEstimate(Pose2d visionPose) {
        this.poseEstimator.addVisionMeasurement(visionPose, Timer.getFPGATimestamp());
    }

    /**
     * Set max allowed current for throttle in amps
     */
    public void setMaxThrottleCurrent(int amps) {
        modules[0].setMaxThrottleCurrent(amps);
        modules[1].setMaxThrottleCurrent(amps);
        modules[2].setMaxThrottleCurrent(amps);
        modules[3].setMaxThrottleCurrent(amps);
    }

    /**
     * Set max allowed current for steer in amps
     */
    public void setMaxSteerCurrent(int amps) {
        modules[0].setMaxSteerCurrent(amps);
        modules[1].setMaxSteerCurrent(amps);
        modules[2].setMaxSteerCurrent(amps);
        modules[3].setMaxSteerCurrent(amps);
    }

    /**
     * Resets the current gyroscope yaw to the specified value 
     */
    public void setYaw(double degrees) {
        this.gyro.setYaw(degrees);
    }

    /**
     * The pitch of the gyroscope
     */
    public double getPitch() {
        return gyro.getPitch();
    }

    /**
     * The roll of the gyroscope
     */
    public double getRoll() {
        return gyro.getRoll();
    }

    /**
     * The yaw of the gyroscope
     */
    public Rotation2d getYaw() {
        return Rotation2d.fromDegrees(this.gyro.getYaw());
    }

    /**
     * The current position of the robot on the field based on odometry readings
     */
    public Pose2d getCurrentPose() {
        return odometry.getPoseMeters();
    }

    /**
     * Resets the position of the robot on the field to the specified Pose2d
     */
    public void resetPose(Pose2d pose) {
        odometry.resetPosition(new Rotation2d(0),
                new SwerveModulePosition[] { new SwerveModulePosition(), new SwerveModulePosition(),
                        new SwerveModulePosition(), new SwerveModulePosition() },
                pose);
    }

    /**
     * The state of each of the swerve modules
     */
    public SwerveModuleState[] getStates() {
        SwerveModuleState[] states = new SwerveModuleState[modules.length];
        for (int i = 0; i < modules.length; i++) {
            states[i] = new SwerveModuleState(
                modules[i].getDriveVelocity(), 
                Rotation2d.fromDegrees(modules[i].getSteerAngle())
            );
        }
        return states;
    }

    /**
     * The position of each of the swerve modules
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[modules.length];
        for (int i = 0; i < modules.length; i++) {
            positions[i] = modules[i].getModulePosition();
        }
        return positions;
    }

    /**
     * Returns current translational and rotational speeds of the swerve
     */
    public ChassisSpeeds getChassisSpeeds() {
        return kinematics.toChassisSpeeds(
            modules[0].getModuleState(),
            modules[1].getModuleState(),
            modules[2].getModuleState(),
            modules[3].getModuleState()
        );
    }

    /**
     * Applies a chassis speeds object to wheel speeds
     */
    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(states, profile.maxSpeed);

        modules[0].set(states[0]); 
        modules[1].set(states[1]);
        modules[2].set(states[2]);
        modules[3].set(states[3]);

        this.odometry.update(this.getYaw(),
            new SwerveModulePosition[] { 
                    modules[0].getModulePosition(),
                    modules[1].getModulePosition(), 
                    modules[2].getModulePosition(),
                    modules[3].getModulePosition()
            }
        );

        this.currentState = new ChassisState(
            getChassisSpeeds(), 
            getCurrentPose()
        );
    }

    /**
     * Locks the wheels in an 'X' pattern
     */
    public void lockWheels() {
        modules[0].set(new SwerveModuleState(0.0, Rotation2d.fromDegrees(-45)));
        modules[1].set(new SwerveModuleState(0.0, Rotation2d.fromDegrees(45)));
        modules[2].set(new SwerveModuleState(0.0, Rotation2d.fromDegrees(-45)));
        modules[3].set(new SwerveModuleState(0.0, Rotation2d.fromDegrees(45)));
    }

    /**
     * Current state of the chassis
     */
    public ChassisState getCurrentChassisState() {
        return this.currentState;
    }

    public static class SwerveProfile {
        public double width, length, maxSpeed;

        /**
         * @param width Left-to-right distance between the wheels
         * @param length Front-to-back distance between the wheels
         * @param maxSpeed Maximum allowed velocity for the swerve (m/s)
         */
        public SwerveProfile(double width, double length, double maxSpeed) {
            this.width = width;
            this.length = length;
            this.maxSpeed = maxSpeed;
        }
    }
}