package frc.robot.modules;

import static frc.robot.common.AprilTags.*;

import frc.robot.common.Constants;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.Limelight;
import frc.robot.hardware.OakCamera;

public class VisionModule extends Module {
    private static VisionModule INSTANCE = null;
    public static VisionModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionModule();
        }
        return INSTANCE;
    }

    private Limelight frontLimelight;
    private OakCamera rearOak;

    private VisionModule() {
        frontLimelight = new Limelight(Constants.VISION.FRONT_LIMELIGHT_NAME);
        rearOak = new OakCamera();
    }

    /**
     * Distance to the centered april tag on the speaker
     */
    public double getDistanceToSpeaker() {
        return 3.0;
        // return rearOak.distanceToAprilTag(List.of(4, 7)); // TODO - How about not hard code this?
    }

    public boolean isSpeakerTargetVisible() {
        return rearOak.getCurrTagID() == RED_SPEAKER_CENTER.id() || 
            rearOak.getCurrTagID() == BLUE_SPEAKER_CENTER.id() || 
            rearOak.getCurrTag2ID() == RED_SPEAKER_CENTER.id() || 
            rearOak.getCurrTag2ID() == BLUE_SPEAKER_CENTER.id();
    }
    
    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}