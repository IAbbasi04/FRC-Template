package frc.robot.autonomous;

import com.pathplanner.lib.auto.*;
import com.pathplanner.lib.path.*;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.autonomous.commands.DelayCommand;

public class AutonomousBuilder {
    public enum StartSide {
        kAmp(AutoGenerator.SUBWOOFER_AMP.getPose()),
        kCenter(AutoGenerator.SUBWOOFER_MIDDLE.getPose()),
        kSource(AutoGenerator.SUBWOOFER_SOURCE.getPose());

        public Pose2d pose;
        private StartSide(Pose2d pose) {
            this.pose = pose;
        }
    }

    public enum EndMode {
        kSit,
        kRush
    }

    public enum ScoreablePiece {
        kNone,
        kWing1,
        kWing2,
        kWing3,
        kMid1,
        kMid2,
        kMid3,
        kMid4,
        kMid5,
    }

    public ShuffleboardTab autonTab = Shuffleboard.getTab("Auton Builder");
    private SendableChooser<StartSide> sideChooser = new SendableChooser<>();
    private SendableChooser<ScoreablePiece> pieceChooser = new SendableChooser<>();
    private SendableChooser<Boolean> preloadChooser = new SendableChooser<>();
    
    private boolean shootPreload = true;

    private Pose2d startPose = new Pose2d();

    public AutonomousBuilder() {
        sideChooser.setDefaultOption("Center", StartSide.kCenter);
        pieceChooser.setDefaultOption("None", ScoreablePiece.kNone);

        preloadChooser.setDefaultOption("True", true);
        preloadChooser.addOption("False", false);
        
        for (StartSide side : StartSide.values()) {
            sideChooser.addOption(side.name().substring(1), side);
        }

        for (ScoreablePiece piece : ScoreablePiece.values()) {
            pieceChooser.addOption(piece.name().substring(1), piece);
        }

        autonTab.add("Choose Side", sideChooser)
            .withPosition(2, 2)
            .withSize(2, 2);

        autonTab.add("Choose First Piece", pieceChooser)
            .withPosition(5, 2)
            .withSize(2, 2);

        autonTab.add("Shoot Preload?", preloadChooser)
            .withPosition(8, 2)
            .withSize(2, 2);
    }

    public Command buildAuto() {
        String side = ((StartSide)sideChooser.getSelected()).name().substring(1);
        String piece = ((ScoreablePiece)pieceChooser.getSelected()).name().substring(1);
        String auto = "Speaker " + 
            side + 
            " To " +
            piece.substring(0, piece.length() - 1) + " Note " + 
            piece.substring(piece.length() - 1)
        ;

        SequentialCommandGroup autoCommand = new SequentialCommandGroup(
            new ConditionalCommand( // Shoot preload if we want
                NamedCommands.getCommand("SubwooferShoot"),
                new DelayCommand(0),
                () -> shootPreload
            ),
            new ParallelDeadlineGroup(
                NamedCommands.getCommand("Intake"),
                AutoBuilder.followPath(PathPlannerPath.fromPathFile(auto))
            )
        );

        if (piece.equals("None")) {
            startPose = ((StartSide)sideChooser.getSelected()).pose;
        } else {
            startPose = PathPlannerPath.fromPathFile(auto).getPreviewStartingHolonomicPose();
        }
        
        return autoCommand;
    }

    public Pose2d getStartPose() {
        return startPose;
    }
}