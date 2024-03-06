package frc.robot.autonomous.autos;

import frc.robot.autonomous.BaseAuto;
import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.commands.CommandQueue;
import frc.robot.commands.FollowerCommand;

import static frc.robot.autonomous.AutoGenerator.*;

public class MobilityAuto extends BaseAuto {
    private SwerveTrajectory MOBILITY = generate(
        slowConfig, 
        SUBWOOFER_MIDDLE.getPose(),
        WING_NOTE_2.getPose()
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new FollowerCommand(MOBILITY)
        );
    }
}
