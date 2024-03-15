package frc.robot.autonomous.autos.center;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.commands.*;
import frc.robot.autonomous.commands.DeliverCommand.DeliverType;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

public class Center3Wing1MidAuto extends Center3WingAuto {
    private SwerveTrajectory WING_NOTE_1_TO_MID_NOTE_1 = generate(
        createDefaultTrajectoryConfig(fastConfig), 
        WING_NOTE_1.translate(-0.25, 0.0),
        MID_NOTE_1.translate(-0.5, 0.0)
    );

    private SwerveTrajectory MID_NOTE_1_TO_WING = generate(
        createReverseTrajectoryConfig(fastConfig), 
        MID_NOTE_1.translate(-0.5, 0.0),
        WING_SCORE.getPose()
    );

    @Override
    public void initialize() {
        super.initialize();
        queue.append(
            new JointCommand(
                new FollowerCommand(WING_NOTE_1_TO_MID_NOTE_1),
                new IntakeCommand()
            ).setTag("INTAKING FIFTH NOTE"),
            new FollowerCommand(MID_NOTE_1_TO_WING).setTag("MOVE BACK TO WING"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE FIFTH NOTE")
        );
    }
}