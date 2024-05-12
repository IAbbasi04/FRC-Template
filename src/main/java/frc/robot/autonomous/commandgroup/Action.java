package frc.robot.autonomous.commandgroup;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Multiple commands running in a specific order.
 * 
 * <p> i.e. IntakeNoteAction.java runs the intake and moves to the specified note
 */
public abstract class Action {
    /**
     * Compiles the entire action into a command to send to the CommandScheduler
     */
    public abstract Command asSendableCommand();
}