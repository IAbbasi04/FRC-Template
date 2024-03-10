package frc.robot.autonomous.autos.ampside;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import frc.robot.autonomous.commands.DeliverCommand.DeliverType;
import edu.wpi.first.math.geometry.Rotation2d;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

public class AmpSide1Wing2MidAuto extends BaseAuto {
    private SwerveTrajectory SPEAKER_AMP_TO_WING_NOTE_1 = generate(
        createDefaultTrajectoryConfig(fastConfig), 
        SUBWOOFER_AMP.rotate(Rotation2d.fromDegrees(10)),
        WING_NOTE_1.translate(-0.5, 0.0)
    ).setStartAngle(Rotation2d.fromDegrees(60));

    private SwerveTrajectory WING_NOTE_1_TO_MID_NOTE_1 = generate(
        createDefaultTrajectoryConfig(fastConfig), 
        WING_NOTE_1.translate(-0.5, 0.0),
        MID_NOTE_1.translate(-0.5, 0.0)
    );

    private SwerveTrajectory MID_NOTE_1_TO_WING = generate(
        createReverseTrajectoryConfig(fastConfig), 
        MID_NOTE_1.translate(-0.5, 0.0),
        WING_SCORE.getPose()
    );

    private SwerveTrajectory WING_TO_MID_NOTE_2 = generate(
        createDefaultTrajectoryConfig(fastConfig), 
        WING_SCORE.getPose(),
        MID_NOTE_2.translate(-0.5, 0.0)
    );

    private SwerveTrajectory MID_NOTE_2_TO_WING = generate(
        createReverseTrajectoryConfig(fastConfig), 
        MID_NOTE_2.translate(-0.5, 0.0),
        WING_SCORE.getPose()
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new DeliverCommand(DeliverType.SPEAKER).setTag("SCORE PRELOAD NOTE"),
            new JointCommand(
                new FollowerCommand(SPEAKER_AMP_TO_WING_NOTE_1),
                new IntakeCommand()
            ).setTag("INTAKE SECOND NOTE"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE SECOND NOTE"),
            new JointCommand(
                new FollowerCommand(WING_NOTE_1_TO_MID_NOTE_1),
                new IntakeCommand()
            ).setTag("INTAKE THIRD NOTE"),
            new FollowerCommand(MID_NOTE_1_TO_WING).setTag("MOVE BACK TO WING"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE THIRD NOTE"),
            new JointCommand(
                new FollowerCommand(WING_TO_MID_NOTE_2),
                new IntakeCommand()
            ).setTag("INTAKE FOURTH NOTE"),
            new FollowerCommand(MID_NOTE_2_TO_WING).setTag("MOVE BACK TO WING"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE FOURTH NOTE")
        );
    }
    
}