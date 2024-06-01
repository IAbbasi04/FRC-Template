package frc.robot.autonomous;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public enum AutoGenerator {
    
    ;

    private static double RED_WALL_X = 16.542; // According to simulation

    private double x, y;
    private Rotation2d rotation;

    AutoGenerator(double p_x, double p_y) {
        this(p_x, p_y, new Rotation2d());
    }

    AutoGenerator(double p_x, double p_y, Rotation2d p_rot) {
        x = p_x;
        y = p_y;
        rotation = p_rot;
    }

    /**
     * Rotates a position by a certain angle offset
     */
    public Pose2d rotate(Rotation2d p_rot) {
        return new Pose2d(x, y, p_rot);
    }

    /**
     * Translates a position by a certain x, y
     */
    public Pose2d translate(double p_x, double p_y) {
        return new Pose2d(x + p_x, y + p_y, rotation);
    }

    /**
     * Translates a position by a certain x, y, and rotational offset
     */
    public Pose2d translate(double p_x, double p_y, Rotation2d p_rot) {
        return new Pose2d(x + p_x, y + p_y, p_rot);
    }

    /**
     * Gets the field pose of this position
     * @return
     */
    public Pose2d getPose() {
        return new Pose2d(x, y, rotation);
    }

    /**
     * Creates a spline trajectory using a specific config and multiple poses
     */
    public static SwerveTrajectory generate(TrajectoryConfig config, Pose2d ... poses) {
        Translation2d[] interiorPoses = new Translation2d[poses.length - 2];
        boolean isRed = DriverStation.getAlliance().get() != Alliance.Blue;
        Pose2d startPose = !isRed ? poses[0] : new Pose2d(RED_WALL_X - poses[0].getX(), poses[0].getY(), Rotation2d.fromDegrees(180).minus(poses[0].getRotation()));
        Pose2d endPose = !isRed ? poses[poses.length-1] : new Pose2d(RED_WALL_X - poses[poses.length-1].getX(), poses[poses.length-1].getY(), Rotation2d.fromDegrees(180).minus(poses[poses.length-1].getRotation()));

        for (int i = 1; i < poses.length-1; i++) {
            double red_x = RED_WALL_X - poses[i].getX();
            Translation2d redTranslation = new Translation2d(red_x, poses[i].getY());
            interiorPoses[i-1] = isRed ? redTranslation : poses[i].getTranslation();
        }

        return new SwerveTrajectory(TrajectoryGenerator.generateTrajectory(
            startPose,
            List.of(interiorPoses),
            endPose,
            config
        )).setStartAngle(poses[0].getRotation());
    }
}