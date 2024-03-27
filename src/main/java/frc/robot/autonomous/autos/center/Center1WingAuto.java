package frc.robot.autonomous.autos.center;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import static frc.robot.autonomous.AutoGenerator.*;

import frc.robot.Robot;

public class Center1WingAuto extends BaseAuto {
    private SwerveTrajectory SUBWOOFER_MID_TO_WING_NOTE_2 = generate(
        slowConfig, 
        SUBWOOFER_MIDDLE.getPose(),
        WING_NOTE_2.getPose()
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(1.40))
                .setTag("SCORE PRELOAD NOTE"),
            new JointCommand(
                new FollowerCommand(SUBWOOFER_MID_TO_WING_NOTE_2),
                new IntakeCommand()
            ).setTag("DRIVE TO AND INTAKE SECOND NOTE"),
            new ShootCommand(Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(1.8))
                .setTag("SCORE SECOND NOTE")
        );
    }
}