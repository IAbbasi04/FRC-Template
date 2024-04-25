package frc.robot.autonomous.newtonCommands;

import com.lib.team8592.Utils;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.modules.ExampleModule;

public class ExampleCommand extends NewtonCommand {
    private Timer exampleTimer;
    private double desiredVelocity;

    public ExampleCommand(double desiredVelocity) {
        this.desiredVelocity = desiredVelocity;
    }

    @Override
    public void initialize() {
        exampleTimer = new Timer();
        exampleTimer.reset();
        exampleTimer.start();
    }

    @Override
    public void execute() {
        ExampleModule.getInstance().setDesiredVelocity(desiredVelocity);
    }

    @Override
    public boolean isFinished() {
        return 
        Utils.isWithin(ExampleModule.getInstance().getExampleVelocity(), desiredVelocity, startDelay) ||
        (Robot.isSimulation() && commandTimer.get() >= 0.50); // Assume this command takes 0.50 seconds
    }

    @Override
    public void end(boolean interrupted) {
        ExampleModule.getInstance().setDesiredVelocity(0.0);
    }
}