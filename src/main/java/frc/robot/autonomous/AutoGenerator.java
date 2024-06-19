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
    SPEAKER_OPENING(0.25, 5.50),

    SUBWOOFER_AMP(0.675, 6.75, Rotation2d.fromDegrees(60)),
    SUBWOOFER_MIDDLE(1.385, 5.585),
    SUBWOOFER_SOURCE(0.675, 4.345, Rotation2d.fromDegrees(-60)),

    AMP(1.82, 7.65, Rotation2d.fromDegrees(90)),

    STAGE(5.0, 4.0),

    WING_NOTE_1(2.90, 7.00),
    WING_NOTE_2(2.90, 5.585),
    WING_NOTE_3(2.90, 4.345),
    
    MID_NOTE_1(8.30, 7.475),
    MID_NOTE_2(8.30, 5.775),
    MID_NOTE_3(8.30, 4.125),
    MID_NOTE_4(8.30, 2.45),
    MID_NOTE_5(8.30, 0.75),

    WING_SCORE(6.0, 7.125 - 0.3),
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