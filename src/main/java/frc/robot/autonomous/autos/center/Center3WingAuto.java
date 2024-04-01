package frc.robot.autonomous.autos.center;

import frc.robot.Robot;
import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.SwerveTrajectory.RotationProfile;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import edu.wpi.first.math.geometry.Rotation2d;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

public class Center3WingAuto extends BaseAuto {
    private SwerveTrajectory SPEAKER_MID_TO_WING_NOTE_3 = generate(
        createDefaultTrajectoryConfig(movingScoreConfig),
        SUBWOOFER_MIDDLE.getPose(),
        WING_NOTE_3.translate(-0.25, 0, Rotation2d.fromDegrees(-45))
    ).withRotation(new RotationProfile(Rotation2d.fromDegrees(-45)).delay(0.25));

    private SwerveTrajectory WING_NOTE_3_TO_WING_NOTE_2 = generate(
        createDefaultTrajectoryConfig(movingScoreConfig),
        WING_NOTE_3.translate(-0.25, 0, Rotation2d.fromDegrees(180)),
        WING_NOTE_3.translate(-0.75, 0.75),
        WING_NOTE_2.translate(-0.25, 0.0)
    ).withRotation(new RotationProfile(Rotation2d.fromDegrees(0)).delay(0.25));

    private SwerveTrajectory WING_NOTE_2_TO_WING_NOTE_1 = generate(
        createDefaultTrajectoryConfig(movingScoreConfig),
        WING_NOTE_2.translate(-0.25, 0.0, Rotation2d.fromDegrees(180)),
        WING_NOTE_2.translate(-0.75, 0.5),
        WING_NOTE_1.translate(-0.25, 0.0, Rotation2d.fromDegrees(30))
    ).withRotation(new RotationProfile(Rotation2d.fromDegrees(30)).delay(0.25));

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(1.40))
                .setTag("SCORE PRELOAD"),
            new JointCommand(
                new IntakeCommand(),
                new FollowerCommand(SPEAKER_MID_TO_WING_NOTE_3)
            ).setTag("INTAKE SECOND NOTE"),
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(2.0))
                .setTag("SCORE SECOND NOTE"),
            new JointCommand(
                new IntakeCommand(),
                new FollowerCommand(WING_NOTE_3_TO_WING_NOTE_2)
            ).setTag("INTAKE THIRD NOTE"),
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(1.8))
                .setTag("SCORE THIRD NOTE"),
            new JointCommand(
                new IntakeCommand(),
                new FollowerCommand(WING_NOTE_2_TO_WING_NOTE_1)
            ).setTag("INTAKE FOURTH NOTE"),
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(2.0))
                .setTag("SCORE FOURTH NOTE")
        );
    }
}