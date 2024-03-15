package frc.robot.autonomous.autos.ampside;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import frc.robot.autonomous.commands.DeliverCommand.DeliverType;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

public class AmpSidePreloadMobilityAuto extends BaseAuto {
    private SwerveTrajectory SPEAKER_AMP_TO_MOBILITY = generate(
        createDefaultTrajectoryConfig(slowConfig), 
        SUBWOOFER_AMP.getPose(),
        WING_NOTE_1.translate(0.0, 0.5),
        WING_SCORE.translate(0.0, 0.75)
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new DeliverCommand(DeliverType.SPEAKER).setTag("SCORE PRELOAD"),
            new FollowerCommand(SPEAKER_AMP_TO_MOBILITY).setTag("MOBILITY")
        );
    }
}