package frc.robot.autonomous.autos.sourceside;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import static frc.robot.autonomous.AutoGenerator.*;
import static frc.robot.autonomous.TrajectoryUtil.*;

import frc.robot.Robot;

public class SourceSidePreloadMobilityAuto extends BaseAuto {
    private SwerveTrajectory SPEAKER_SOURCE_TO_MOBILITY = generate(
        createDefaultTrajectoryConfig(slowConfig), 
        SUBWOOFER_SOURCE.getPose(),
        MID_NOTE_5.translate(-2.0, 0)
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(1.40)).setTag("SCORE PRELOAD"),
            new FollowerCommand(SPEAKER_SOURCE_TO_MOBILITY).setTag("MOBILITY")
        );
    }
}