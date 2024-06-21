package frc.robot.autonomous;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autonomous.autos.*;
import frc.robot.autonomous.autos.ampside.*;
import frc.robot.autonomous.autos.centerside.*;
import frc.robot.autonomous.autos.sourceside.*;

public class AutonomousSelector {
    public ShuffleboardTab autonTab = Shuffleboard.getTab("Auton Configuration");
    private SendableChooser<Class<?>> autonChooser = new SendableChooser<>();
    private GenericEntry delayEntry;

    public Class<?>[] autos = {
        // Put all autos in here
        AmpSideWing1Mid12Auto.class, // 4 Note Auto

        CenterWing32Mid3Auto.class, // 3 Note Auto
        CenterWing321Auto.class, // 4 Note Auto

        SourceSideMid54Auto.class, // 2 Note Auto
    };

    public AutonomousSelector() {
        autonChooser.setDefaultOption("DEFAULT - DO NOTHING", DoNothingAuto.class);
        for (Class<?> auto : autos) {
            autonChooser.addOption(auto.getSimpleName(), auto);
        }

        autonTab.add("Choose Autonomous", autonChooser)
            .withPosition(3, 2)
            .withSize(4, 2);

        delayEntry = autonTab.add("Autonomous Delay", 0d)
            .withPosition(4, 1)
            .withSize(2, 1)
            .getEntry();
    }

    public double getDelay() {
        return delayEntry.getDouble(0.0);
    }

    public NewtonAuto getSelectedAutonomous() {
        try {
            NewtonAuto selected = (NewtonAuto) autonChooser.getSelected().getDeclaredConstructor().newInstance();
            return selected;
        } catch (Exception e) {
            return null;
        }
    }
}