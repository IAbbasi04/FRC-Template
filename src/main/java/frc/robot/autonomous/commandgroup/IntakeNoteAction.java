package frc.robot.autonomous.commandgroup;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomous.commands.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.*;
import static frc.robot.autonomous.AutoGenerator.*;

public class IntakeNoteAction extends Action{
    public enum Note {
        WING_1(WING_NOTE_1.getPose()),
        WING_2(WING_NOTE_2.getPose()),
        WING_3(WING_NOTE_3.getPose()),

        MID_1(MID_NOTE_1.getPose()),
        MID_2(MID_NOTE_2.getPose()),
        MID_3(MID_NOTE_3.getPose()),
        MID_4(MID_NOTE_4.getPose()),
        MID_5(MID_NOTE_5.getPose());

        public Pose2d pose;
        private Note(Pose2d pose) {
            this.pose = pose;
        }
    }

    private Note note;

    public IntakeNoteAction(Note note) {
        this.note = note;
    }

    @Override
    public Command asSendableCommand() {
        return AutoBuilder.pathfindToPoseFlipped(
            this.note.pose,
            new PathConstraints(4, 3, 4*Math.PI, 2*Math.PI)
        ).raceWith(new IntakeCommand());
    }
}