package frc.robot.subsystems;

public class Superstructure {
    /**
     * Intake a note from the ground
     */
    public static void setIntaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(3000);
        IntakeSubsystem.getInstance().setFeederVelocity(3000);
        IntakeSubsystem.getInstance().setArmAngle(60.0);
    }

    /**
     * Place a note in front of the robot
     */
    public static void setPlacing() {
        IntakeSubsystem.getInstance().setRollerVelocity(-3000);
        IntakeSubsystem.getInstance().setArmAngle(60.0);
        if (IntakeSubsystem.getInstance().getArmPosition() >= 45) {
            // Only spit note if arm past certain threshold
            IntakeSubsystem.getInstance().setFeederVelocity(-3000);
        }
    }

    /**
     * Turns off intake motor and feeder motor
     */
    public static void setIntakeRollersOff() {
        IntakeSubsystem.getInstance().setRollerVelocity(0.0);
        IntakeSubsystem.getInstance().setFeederVelocity(0.0);
    }

    /**
     * Stop spinning the intake and retract the arm
     */
    public static void setIntakeGroundState() {
        setIntakeRollersOff();
        IntakeSubsystem.getInstance().setArmAngle(0.0);
    }
}