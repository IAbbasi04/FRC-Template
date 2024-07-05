package frc.robot.autonomous.commands.crescendo;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.autonomous.commands.NewtonCommand;
import frc.robot.crescendo.ShotProfile;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Superstructure.Superstate;

public class PrimeCommand extends NewtonCommand {
    private ShotProfile shotProfile;
    private Timer timer = new Timer();
    private boolean isShooting = false;

    public PrimeCommand() {
        this.shotProfile = null;
    }

    public PrimeCommand(ShotProfile shotProfile) {
        this.shotProfile = shotProfile;
    }

    @Override
    public void execute() {
        if (this.shotProfile == null) { // Ranged shot
            double distanceToTarget = VisionSubsystem.getInstance().getDistanceToSpeaker();
            shotProfile = Robot.SHOT_TABLE.getShotFromDistance(distanceToTarget);
        }

        shotProfile = shotProfile.shouldShoot(false); // Disable shooting

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
        return false; // Always return false since this command is mainly a passthrough command
    }
}