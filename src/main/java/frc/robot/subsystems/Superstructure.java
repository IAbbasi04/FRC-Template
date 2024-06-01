package frc.robot.subsystems;

public class Superstructure {
    private static Superstructure INSTANCE;
    public static Superstructure getInstance() {
        if (INSTANCE == null) INSTANCE = new Superstructure();
        return INSTANCE;
    }

    // Add all inter-mechanism methods here
}