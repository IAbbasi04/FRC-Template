package frc.robot.autonomous.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.modes.ModeManager;
import frc.robot.modules.ElevatorModule;
import frc.robot.modules.ShooterModule;
import frc.robot.modules.VisionModule;

public class ShootCommand extends Command {
    private ShotProfile fallbackShotProfile;
    private Timer shotTimer;

    public ShootCommand(ShotProfile fallbackShotProfile) {
        this.fallbackShotProfile = fallbackShotProfile;
        shotTimer = new Timer();
    }

    @Override
    public void initialize() {
        shotTimer.reset();
        shotTimer.stop();
    }

    @Override
    public boolean execute() {
        ShotProfile actualShotProfile;
        double distanceToTag = VisionModule.getInstance().getDistanceToSpeaker();
        if (distanceToTag == -1) { // Tag not visible
            actualShotProfile = fallbackShotProfile;
        } else {
            actualShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(distanceToTag);
        }

        ModeManager.setShooting(actualShotProfile); // Shoot with all subsystems
        if (ShooterModule.getInstance().atTargetVelocity() && ElevatorModule.getInstance().atTargetPosition()) {
            // Timer starts after ready to shoot
            shotTimer.start();
        }

        return shotTimer.get() >= 0.75 || commandTimer.get() >= 5.0; // Automatic timeout if taking longer than 5 seconds
    }

    @Override
    public void shutdown() {
        ModeManager.setGroundState();
        ModeManager.stopFeeder();
        ModeManager.stopShooter();
    }
}