package frc.robot.autonomous.commands;

import frc.robot.Robot;
import frc.robot.common.crescendo.ShotProfile;
import frc.robot.modes.ModeManager;
import frc.robot.modules.VisionModule;

public class PrimeCommand extends Command {
    private ShotProfile staticShot;
    private boolean useVision;

    public PrimeCommand(ShotProfile staticShot, boolean useVision) {
        this.staticShot = staticShot;
        this.useVision = useVision;
    }

    @Override
    public void initialize() {}

    @Override
    public boolean execute() {
        ShotProfile desiredShot = staticShot;
        double distanceToTag = VisionModule.getInstance().getDistanceToSpeaker();
        if (useVision && distanceToTag != -1) { // If you want to use vision and the speaker tag is visible
            desiredShot = Robot.UNDEFENDED_SHOT_TABLE.getShotFromDistance(distanceToTag);
        }
        ModeManager.setShooting(desiredShot.shouldShoot(false));
        return true;
    }

    @Override
    public void shutdown() {
        // No shutdown intentional since you want the priming to carry over in command queue
    }
}