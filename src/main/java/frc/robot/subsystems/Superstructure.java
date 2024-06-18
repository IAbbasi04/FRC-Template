package frc.robot.subsystems;

/**
 * Class designed for holding inter-mechanism code and logic
 * 
 * This class is used in all match modes for increased consistency
 * and reliability between all modes
 * 
 * Typically, game functions only requiring a single mechanism 
 * can be dealt with inside its respective mechanism class
 */
public class Superstructure {
    /**
     * Intake a note from the ground
     */
    public static void setIntaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(3000);
    }

    /**
     * Places a note back on the ground
     */
    public static void setOutaking() {
        IntakeSubsystem.getInstance().setRollerVelocity(-3000);
    }

    /**
     * Places a note back on the ground
     */
    public static void stopIntake() {
        IntakeSubsystem.getInstance().setRollerVelocity(0.0);
    }
}