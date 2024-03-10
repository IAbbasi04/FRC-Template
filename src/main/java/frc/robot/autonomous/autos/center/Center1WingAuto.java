package frc.robot.autonomous.autos.center;

import frc.robot.autonomous.SwerveTrajectory;
import frc.robot.autonomous.autos.BaseAuto;
import frc.robot.autonomous.commands.*;
import frc.robot.autonomous.commands.DeliverCommand.DeliverType;
import static frc.robot.autonomous.AutoGenerator.*;

public class Center1WingAuto extends BaseAuto {
    private SwerveTrajectory SUBWOOFER_MID_TO_WING_NOTE_2 = generate(
        slowConfig, 
        SUBWOOFER_MIDDLE.getPose(),
        WING_NOTE_2.getPose()
    );

    @Override
    public void initialize() {
        queue = new CommandQueue(
            new DeliverCommand(DeliverType.SPEAKER).setTag("SCORE PRELOAD NOTE"),
            new JointCommand(
                new FollowerCommand(SUBWOOFER_MID_TO_WING_NOTE_2),
                new IntakeCommand()
            ).setTag("DRIVE TO AND INTAKE SECOND NOTE"),
            new DeliverCommand(DeliverType.SPEAKER)
                .addPoseTarget(SPEAKER_OPENING.getPose())    
                .setTag("SCORE SECOND NOTE")
        );
    }
}