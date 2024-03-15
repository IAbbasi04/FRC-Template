package frc.robot.autonomous.autos.sourceside;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.SwerveTrajectory.RotationProfile;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import frc.robot.autonomous.commands.DeliverCommand.DeliverType;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

import edu.wpi.first.math.geometry.Rotation2d;

public class SourceSide2MidAuto extends BaseAuto {
    private SwerveTrajectory SPEAKER_SOURCE_TO_MID_NOTE_5 = generate(
        createDefaultTrajectoryConfig(fastConfig),
        SUBWOOFER_SOURCE.getPose(),
        MID_NOTE_5.translate(-0.5, 0.0)
    ).withRotation(new RotationProfile().delay(0.25));

    private SwerveTrajectory MID_NOTE_5_TO_WING = generate(
        createReverseTrajectoryConfig(fastConfig),
        MID_NOTE_5.translate(-0.5, 0.0),
        WING_NOTE_3.translate(0.5, -2.0, Rotation2d.fromDegrees(-45))
    ).withRotation(new RotationProfile(Rotation2d.fromDegrees(-45)).delay(1.0));
    
    private SwerveTrajectory WING_TO_MID_NOTE_4 = generate(
        createDefaultTrajectoryConfig(fastConfig),
        WING_NOTE_3.translate(0.5, -2.0, Rotation2d.fromDegrees(-45)),
        MID_NOTE_4.translate(-0.5, 0, Rotation2d.fromDegrees(45))
    ).withRotation(new RotationProfile(Rotation2d.fromDegrees(45)).delay(1.0));

    private SwerveTrajectory MID_NOTE_4_TO_WING = generate(
        createReverseTrajectoryConfig(fastConfig),
        MID_NOTE_4.translate(-0.5, 0, Rotation2d.fromDegrees(45)),
        WING_NOTE_3.translate(0.5, -2.0, Rotation2d.fromDegrees(-45))
    ).withRotation(new RotationProfile(Rotation2d.fromDegrees(-45)).delay(1.0));

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new DeliverCommand(DeliverType.SPEAKER).setTag("SCORE PRELOAD"),
            new JointCommand(
                new FollowerCommand(SPEAKER_SOURCE_TO_MID_NOTE_5),
                new IntakeCommand()
            ).setTag("INTAKE SECOND NOTE"),
            new FollowerCommand(MID_NOTE_5_TO_WING).setTag("MOVE BACK TO WING"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE SECOND NOTE"),
            new JointCommand(
                new FollowerCommand(WING_TO_MID_NOTE_4),
                new IntakeCommand()
            ).setTag("INTAKE THIRD NOTE"),
            new FollowerCommand(MID_NOTE_4_TO_WING).setTag("MOVE BACK TO WING"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE THIRD NOTE")
        );
    }
}