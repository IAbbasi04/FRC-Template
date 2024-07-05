package frc.robot.autonomous.commands.crescendo;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.autonomous.commands.NewtonCommand;
import frc.robot.crescendo.ShotProfile;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.Superstructure.Superstate;

public class ScoreCommand extends NewtonCommand {
    private ShotProfile shotProfile;
    private Timer timer = new Timer();
    private boolean isShooting = false;

    public ScoreCommand() {
        this.shotProfile = null;
    }

    public ScoreCommand(ShotProfile shotProfile) {
        this.shotProfile = shotProfile;
    }

    @Override
    public void execute() {
        if (this.shotProfile == null) { // Ranged shot
            double distanceToTarget = VisionSubsystem.getInstance().getDistanceToSpeaker();
            shotProfile = Robot.SHOT_TABLE.getShotFromDistance(distanceToTarget);
        }

        Superstructure.getInstance().setShotProfile(shotProfile);

        isShooting = isShooting || ShooterSubsystem.getInstance().isAtTargetSpeed();
        if (isShooting) {
            timer.start();
        } else {
            timer.reset();
        }
    }

    @Override
    public void end(boolean interrupted) {
        Superstructure.getInstance().setSuperState(Superstate.kStow);
    }

    @Override
    public boolean isFinished() {
        return isShooting && timer.get() >= 0.75;
    }
}