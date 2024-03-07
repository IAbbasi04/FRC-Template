package frc.robot.autonomous.autos.up;

import frc.robot.autonomous.BaseAuto;
import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.commands.CommandQueue;
import frc.robot.autonomous.commands.FollowerCommand;

import static frc.robot.autonomous.AutoGenerator.*;

public class UpPreloadMobility extends BaseAuto {
    private SwerveTrajectory MOBILITY = generate(
        slowConfig, 
        SUBWOOFER_UP.getPose(),
        SUBWOOFER_MIDDLE.translate(4.0, 1.0)
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new FollowerCommand(MOBILITY)
                .setTag("LEAVE WING")
        );
    }
}