package frc.robot.modules;

import com.NewtonSwerve.NewtonSwerve;
import com.NewtonSwerve.SwerveModule;
import com.NewtonSwerve.Gyro.NewtonPigeon2;
import com.NewtonSwerve.Mk4.Mk4ModuleConfiguration;
import com.NewtonSwerve.Mk4.Mk4iSwerveModuleHelper;
import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.common.Ports;
import frc.robot.common.ProfileGains;

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

    private ProfileGains turnToGains = new ProfileGains()
        .setP(0.1)
        .setContinuousInput(0, 360)
        .setTolerance(0.1)
        ;

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

        super.addLogger("Swerve", Robot.LOG_TO_DASHBOARD);
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
     * Turns to the indicated gyroscope rotation
     */
    public void turnToAngle(double degree) {
        desiredSpeeds = new ChassisSpeeds(
            desiredSpeeds.vxMetersPerSecond,
            desiredSpeeds.vyMetersPerSecond,
            turnToGains.toPIDController().calculate(getCurrentRotation().getDegrees(), degree)
        );
    }

    /**
     * The current rotation of the gyro
     */
    public Rotation2d getCurrentRotation() {
        if (!Robot.isReal()) { // Return simulated rotation if in simulation
            return Rotation2d.fromDegrees(Robot.FIELD.getRobotPose().getRotation().getDegrees() % 360);
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
     * Gets the current speed demand of the chassis
     */
    public ChassisSpeeds getDesiredSpeeds() {
        return this.desiredSpeeds;
    }

    /**
     * Sets the desired translational and rotational speeds of the robot 
     */
    public void drive(ChassisSpeeds speeds) {
        this.desiredSpeeds = speeds;
    }

    @Override
    public void init(MatchMode mode) {
        desiredSpeeds = new ChassisSpeeds();
    }

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {
        swerve.drive(desiredSpeeds);

        if (!Robot.isReal() && Robot.MODE != MatchMode.AUTONOMOUS) {
            Pose2d simulatedRobotPose = Robot.FIELD.getRobotPose();
            Pose2d newSimulatedPose = new Pose2d(
                new Translation2d(
                    simulatedRobotPose.getX() - 0.02*desiredSpeeds.vyMetersPerSecond,
                    simulatedRobotPose.getY() + 0.02*desiredSpeeds.vxMetersPerSecond
                ),
                Rotation2d.fromRadians(
                    (simulatedRobotPose.getRotation().getRadians() + 0.02*desiredSpeeds.omegaRadiansPerSecond) % 360
                )
            );

            Robot.FIELD.setRobotPose(newSimulatedPose);
        }
    }
}