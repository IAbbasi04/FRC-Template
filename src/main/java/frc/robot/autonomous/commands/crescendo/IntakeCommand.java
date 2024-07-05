package frc.robot.autonomous.commands.crescendo;

import frc.robot.autonomous.commands.NewtonCommand;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.Superstructure.Superstate;

public class IntakeCommand extends NewtonCommand {
    @Override
    public void initialize() {
        Superstructure.getInstance().setSuperState(Superstate.kIntake);
    }

    @Override
    public boolean isFinished() {
        return FeederSubsystem.getInstance().hasNote(); // Only stop intaking if we have the note
    }

    @Override
    public void end(boolean interrupted) {
        Superstructure.getInstance().setSuperState(Superstate.kStow);
    }
}