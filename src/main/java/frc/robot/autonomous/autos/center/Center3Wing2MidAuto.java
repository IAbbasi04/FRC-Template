package frc.robot.autonomous.autos.center;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.commands.*;
import frc.robot.autonomous.commands.DeliverCommand.DeliverType;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

public class Center3Wing2MidAuto extends Center3Wing1MidAuto {
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
        super.initialize();
        queue.append(
            new JointCommand(
                new FollowerCommand(WING_TO_MID_NOTE_2),
                new IntakeCommand()
            ).setTag("INTAKING SIXTH NOTE"),
            new FollowerCommand(MID_NOTE_2_TO_WING).setTag("MOVE BACK TO WING"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())
                .setTag("SCORE SIXTH NOTE")
        );
    }
}
