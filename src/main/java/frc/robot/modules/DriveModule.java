package frc.robot.modules;

import com.NewtonSwerve.NewtonSwerve;
import com.NewtonSwerve.SwerveModule;
import com.NewtonSwerve.Gyro.NewtonPigeon2;
import com.NewtonSwerve.Mk4.Mk4ModuleConfiguration;
import com.NewtonSwerve.Mk4.Mk4iSwerveModuleHelper;
import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.common.Ports;

public class DriveModule extends Module {
    private static DriveModule INSTANCE = null;
    public static DriveModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriveModule();
        }
        return INSTANCE;
    }

    private NewtonSwerve swerve;
    private ChassisSpeeds desiredSpeeds;

    private DriveModule() {
        Mk4ModuleConfiguration config = new Mk4ModuleConfiguration();

        config.setDriveTrainWidthMeters(Constants.SWERVE.DRIVE_TRAIN_WIDTH);
        config.setDriveTrainLengthMeters(Constants.SWERVE.DRIVE_TRAIN_LENGTH);
        config.setWheelCircumference(Constants.SWERVE.WHEEL_CIRCUMFERENCE);

        config.setNominalVoltage(Constants.SWERVE.MAX_VOLTAGE);
        config.setMaxVelocityMetersPerSecond(Constants.SWERVE.MAX_VELOCITY_METERS_PER_SECOND);

        config.setThrottlePID(
            Constants.SWERVE.THROTTLE_kP,
            Constants.SWERVE.THROTTLE_kI,
            Constants.SWERVE.THROTTLE_kD
        );
        
        config.setSteerPID(
            Constants.SWERVE.STEER_kP, 
            Constants.SWERVE.STEER_kI, 
            Constants.SWERVE.STEER_kD
        );

        SwerveModule frontLeftModule = Mk4iSwerveModuleHelper.createFalcon500(
            config, Mk4iSwerveModuleHelper.GearRatio.L2, 
            Ports.FRONT_LEFT_MODULE_DRIVE_MOTOR_CAN_ID, 
            Ports.FRONT_LEFT_MODULE_STEER_MOTOR_CAN_ID, 
            Ports.FRONT_LEFT_MODULE_STEER_ENCODER_CAN_ID, 
            Constants.SWERVE.FRONT_LEFT_MODULE_STEER_OFFSET
        );

        SwerveModule frontRightModule = Mk4iSwerveModuleHelper.createFalcon500(
            config, Mk4iSwerveModuleHelper.GearRatio.L2, 
            Ports.FRONT_RIGHT_MODULE_DRIVE_MOTOR_CAN_ID, 
            Ports.FRONT_RIGHT_MODULE_STEER_MOTOR_CAN_ID, 
            Ports.FRONT_RIGHT_MODULE_STEER_ENCODER_CAN_ID, 
            Constants.SWERVE.FRONT_RIGHT_MODULE_STEER_OFFSET
        );

        SwerveModule backLeftModule = Mk4iSwerveModuleHelper.createFalcon500(
            config, Mk4iSwerveModuleHelper.GearRatio.L2, 
            Ports.BACK_LEFT_MODULE_DRIVE_MOTOR_CAN_ID, 
            Ports.BACK_LEFT_MODULE_STEER_MOTOR_CAN_ID, 
            Ports.BACK_LEFT_MODULE_STEER_ENCODER_CAN_ID, 
            Constants.SWERVE.BACK_LEFT_MODULE_STEER_OFFSET
        );

        SwerveModule backRightModule = Mk4iSwerveModuleHelper.createFalcon500(
            config, Mk4iSwerveModuleHelper.GearRatio.L2, 
            Ports.BACK_RIGHT_MODULE_DRIVE_MOTOR_CAN_ID, 
            Ports.BACK_RIGHT_MODULE_STEER_MOTOR_CAN_ID, 
            Ports.BACK_RIGHT_MODULE_STEER_ENCODER_CAN_ID, 
            Constants.SWERVE.BACK_RIGHT_MODULE_STEER_OFFSET
        );

        swerve = new NewtonSwerve(
            config,
            new NewtonPigeon2(
                new Pigeon2(Ports.PIGEON_CAN_ID)
            ),
            frontLeftModule,
            frontRightModule,
            backLeftModule,
            backRightModule
        );
    }

    /**
     * Resets the gyroscope to 0
     */
    public void resetGyroscope() {
        swerve.gyro.setYaw(0.0);
    }

    /**
     * Sets the initial position of the robot
     */
    public void setStartPose(Pose2d startPose) {
        swerve.resetPose(startPose);
        swerve.gyro.setYaw(startPose.getRotation().getDegrees());
        if (!Robot.isReal()) {
            Robot.FIELD.setRobotPose(startPose);
        }
    }

    /**
     * The current rotation of the gyro
     */
    public Rotation2d getCurrentRotation() {
        if (!Robot.isReal()) { // Return simulated rotation if in simulation
            return Robot.FIELD.getRobotPose().getRotation();
        }
        return swerve.getGyroscopeRotation();
    }

    /**
     * The current position of the robot
     */
    public Pose2d getCurrentPose() {
        if (!Robot.isReal()) { // Return simulated rotation if in simulation
            return Robot.FIELD.getRobotPose();
        }
        return swerve.getCurrentPos();
    }

    /**
     * Sets the desired translational and rotational speeds of the robot 
     */
    public void drive(ChassisSpeeds speeds) {
        this.desiredSpeeds = speeds;
    }

    @Override
    public void init(MatchMode mode) {
        if (mode == MatchMode.AUTONOMOUS) {
            swerve.gyro.setYaw(0);
        }

        desiredSpeeds = new ChassisSpeeds();
    }

    @Override
    public void periodic() {
        swerve.drive(desiredSpeeds);
    }
}