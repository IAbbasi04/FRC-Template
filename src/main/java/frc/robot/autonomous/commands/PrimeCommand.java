package frc.robot.autonomous.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.modes.ModeManager;
import frc.robot.subsystems.VisionSubsystem;

public class PrimeCommand extends NewtonCommand {
    private ShotProfile fallbackShotProfile;
    private boolean useVision;
    private Timer shotTimer;

    public PrimeCommand() {
        this(Robot.UNDEFENDED_SHOT_TABLE.getStaticShot(), true);
    }

    public PrimeCommand(ShotProfile fallbackShotProfile, boolean useVision) {
        this.fallbackShotProfile = fallbackShotProfile;
        this.useVision = useVision;
        this.shotTimer = new Timer();
    }

    @Override
    public void initialize() {
        shotTimer.reset();
        shotTimer.start();
    }

    @Override
    public void execute() {
        ShotProfile actualShotProfile;
        double distanceToTag = VisionSubsystem.getInstance().getDistanceToSpeaker();
        if (distanceToTag == -1 && useVision) { // Tag not visible
            actualShotProfile = fallbackShotProfile;
        } else {
            actualShotProfile = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(distanceToTag);
        }

        ModeManager.setShooting(actualShotProfile.shouldShoot(false)); // Sets up for shooting without actually shooting
    }

    @Override
    public boolean isFinished() {
        return false; // Always return false since this is mostly a passthrough command
    }

    @Override
    public void end(boolean interrupted) {}
}