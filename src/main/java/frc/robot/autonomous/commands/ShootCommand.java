package frc.robot.autonomous.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.modes.ModeManager;
import frc.robot.subsystems.*;

public class ShootCommand extends NewtonCommand {
    private ShotProfile fallbackShotProfile;
    private boolean useVision;
    private Timer shotTimer;

    public ShootCommand() {
        this(Robot.UNDEFENDED_SHOT_TABLE.getStaticShot(), true);
    }

    public ShootCommand(ShotProfile fallbackShotProfile, boolean useVision) {
        this.fallbackShotProfile = fallbackShotProfile;
        this.useVision = useVision;
        this.shotTimer = new Timer();
    }

    @Override
    public void initialize() {
        shotTimer.reset();
        super.startCommandTimer();
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

        ModeManager.setShooting(actualShotProfile); // Shoot with all subsystems
        if (ShooterSubsystem.getInstance().atTargetVelocity() && ElevatorSubsystem.getInstance().atTargetPosition()) {
            // Possibly add a condition for locked to speaker
            // Timer starts after ready to shoot
            shotTimer.start();
        }
    }

    @Override
    public boolean isFinished() {
        if (Robot.isReal()) {
            return shotTimer.get() >= 0.75 || commandTimer.get() >= 5.0; // Automatic timeout if taking longer than 5 seconds
        } else { // Simulated shot time
            return commandTimer.get() >= 1.25;
        }
    }

    @Override
    public void end(boolean interrupted) {
        ModeManager.setGroundState();
        ModeManager.stopFeeder();
        ModeManager.stopShooter();
    }
}