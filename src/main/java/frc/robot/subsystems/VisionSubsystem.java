package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.PhotonCam;
import lib.frc8592.logging.SmartLogger;

public class VisionSubsystem extends Subsystem {
    private static VisionSubsystem INSTANCE = null;
    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }

    private PhotonCam photonCam;

    private VisionSubsystem() {
        photonCam = new PhotonCam("front-camera");
        super.logger = new SmartLogger("VisionSubsystem");
    }

    /**
     * Gets the yaw offset from the speaker based on the april tag (4 for red, 7 for blue) 
     */
    public Rotation2d getSpeakerYawOffset() {
        int id = 4;
        if (DriverStation.getAlliance().get() == Alliance.Blue) id = 7;
        return photonCam.getYawToAprilTag(id);
    }

    @Override
    public void init(MatchMode mode) {

    }

    @Override
    public void initializeLogs() {

    }

    @Override
    public void periodic() {

    }
}