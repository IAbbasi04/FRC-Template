package frc.robot.subsystems;

import java.util.List;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.common.Constants;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.Limelight;
import lib.frc8592.hardware.OrangePy;
import lib.frc8592.logging.SmartLogger;

import static frc.robot.crescendo.AprilTags.*;

public class VisionSubsystem extends Subsystem {
    private static VisionSubsystem INSTANCE = null;
    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }

    private OrangePy rearOakD;
    private Limelight frontLimelight;

    private VisionSubsystem() {
        rearOakD = new OrangePy(Constants.VISION.REAR_ORANGE_PY_NAME);
        frontLimelight = new Limelight(Constants.VISION.FRONT_LIMELIGHT_NAME);
        super.logger = new SmartLogger("VisionSubsystem");
    }

    /**
     * Distance to the centered april tag on the speaker
     */
    public double getDistanceToSpeaker() {
        double distance = rearOakD.distanceToAprilTag(
            List.of(BLUE_SPEAKER_CENTER.id(), 
                    RED_SPEAKER_CENTER.id()
                )
            );

        if (distance == -1) {
            Pose2d currentPose = SwerveSubsystem.getInstance().getCurrentPose();
            Pose2d targetPose = AutoGenerator.SPEAKER_OPENING.getPose();
            if (DriverStation.getAlliance().isPresent()) {
                if (DriverStation.getAlliance().get() == Alliance.Red) {  
                    targetPose = new Pose2d(
                        new Translation2d(
                            Constants.FIELD.RED_WALL_X - targetPose.getX(),
                            targetPose.getY()
                        ),
                        targetPose.getRotation()
                    );
                }
            }
            Transform2d deltas = targetPose.minus(currentPose);
            distance = Math.sqrt(Math.pow(deltas.getX(), 2) + Math.pow(deltas.getY(), 2));
        }
        return distance;
    }

    /**
     * Yaw offset from speaker
     */
    public Rotation2d getAngleToSpeaker() {
        double offset = rearOakD.getXOffsetFromTag(BLUE_SPEAKER_CENTER.id());
        if (DriverStation.getAlliance().isPresent()) {
            if (DriverStation.getAlliance().get() == Alliance.Red) {  
                offset = rearOakD.getXOffsetFromTag(RED_SPEAKER_CENTER.id());
            }
        }
        
        if (Double.isNaN(offset)) {
            Pose2d currentPose = SwerveSubsystem.getInstance().getCurrentPose();
            Pose2d targetPose = AutoGenerator.SPEAKER_OPENING.getPose();
            if (DriverStation.getAlliance().isPresent()) {
                if (DriverStation.getAlliance().get() == Alliance.Red) {  
                    targetPose = new Pose2d(
                        new Translation2d(
                            Constants.FIELD.RED_WALL_X - targetPose.getX(),
                            targetPose.getY()
                        ),
                        targetPose.getRotation()
                    );
                }
            }
            Transform2d deltas = targetPose.minus(currentPose);
            offset = Units.radiansToDegrees(Math.atan(deltas.getY()/deltas.getX()));
            if (DriverStation.getAlliance().isPresent()) {
                if (DriverStation.getAlliance().get() == Alliance.Red) {
                  offset += 180;
                }
            }
        }
        return Rotation2d.fromDegrees(offset);
    }

    /**
     * Whether the april tag on the speaker is visible or not
     */
    public boolean isSpeakerTargetVisible() {
        return rearOakD.getCurrTagID() == RED_SPEAKER_CENTER.id() || 
            rearOakD.getCurrTagID() == BLUE_SPEAKER_CENTER.id() || 
            rearOakD.getCurrTag2ID() == RED_SPEAKER_CENTER.id() || 
            rearOakD.getCurrTag2ID() == BLUE_SPEAKER_CENTER.id();
    }

    /**
     * Whether a note is in view of the front camera
     */
    public boolean isNoteInView() {
        return frontLimelight.isTargetValid();
    }

    /**
     * Whether the visible note is in suitable targettable
     */
    public boolean isNoteTargettable() {
        return isNoteInView() && frontLimelight.getY() <= -15.0;
    }

    /**
     * The calculated 2d pose of the robot based on April Tag data
     */
    public Pose2d getEstimatedPose() {
        if (rearOakD.getTagInView()) {
            // int tagID = rearOakD.getCurrTagID();
            // double dx = rearOakD.getCurrTagX();
            // double dy = rearOakD.getCurrTagY();
            // double dz = rearOakD.getCurrTagZ();
            // Pose3d tagPose = Constants.FIELD.APRIL_TAG_FIELD_LAYOUT.getTagPose(tagID).get();
            // Pose2d robotPose = new Pose2d(
            //     new Translation2d(
            //         tagPose.getX() - dx,
            //         tagPose.getY() - dy
            //     ),
            //     new Rotation2d()
            // );
            return new Pose2d();
        }
        return null; // Return null if there is no tag visible
    }
    
    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {
        logger.logDouble("Distance to Speaker", () -> getDistanceToSpeaker());
        logger.logDouble("Angle to Speaker", () -> getAngleToSpeaker().getDegrees());
    }

    @Override
    public void periodic() {
        getAngleToSpeaker();
    }
}