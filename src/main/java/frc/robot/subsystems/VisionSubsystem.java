package frc.robot.subsystems;

import java.util.List;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.autonomous.AutoGenerator;
import frc.robot.common.Constants;
import lib.frc8592.MatchMode;
import lib.frc8592.hardware.OakCamera;
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

    private OakCamera rearOak;

    private VisionSubsystem() {
        rearOak = new OakCamera();
        super.logger = new SmartLogger("VisionSubsystem");
    }

    /**
     * Distance to the centered april tag on the speaker
     */
    public double getDistanceToSpeaker() {
        double distance = rearOak.distanceToAprilTag(
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
        double offset = rearOak.getXOffsetFromTag(BLUE_SPEAKER_CENTER.id());
        if (DriverStation.getAlliance().isPresent()) {
            if (DriverStation.getAlliance().get() == Alliance.Red) {  
                offset = rearOak.getXOffsetFromTag(RED_SPEAKER_CENTER.id());
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
        return rearOak.getCurrTagID() == RED_SPEAKER_CENTER.id() || 
            rearOak.getCurrTagID() == BLUE_SPEAKER_CENTER.id() || 
            rearOak.getCurrTag2ID() == RED_SPEAKER_CENTER.id() || 
            rearOak.getCurrTag2ID() == BLUE_SPEAKER_CENTER.id();
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