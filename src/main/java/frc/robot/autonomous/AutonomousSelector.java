package frc.robot.autonomous;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

import frc.robot.autonomous.autos.*;
import frc.robot.autonomous.autos.ampside.*;
import frc.robot.autonomous.autos.center.*;
import frc.robot.autonomous.autos.sourceside.*;

public class AutonomousSelector {
    public ShuffleboardTab autonTab = Shuffleboard.getTab("Auton Configuration");
    private SendableChooser<Class<?>> autonChooser = new SendableChooser<>();
    private GenericEntry delayEntry;

    public Class<?>[] autos = {
        AmpSidePreloadMobilityAuto.class, // 1 speaker | 7 pts
        AmpSide1Wing2MidAuto.class, // 4 speaker | 22 pts

        Center1WingAuto.class, // 2 speaker | 12 pts
        Center3WingAuto.class, // 4 speaker | 22 pts
        Center3Wing1MidAuto.class, // 5 speaker | 27 pts
        Center3Wing2MidAuto.class, // 6 speaker | 32 pts

        SourceSidePreloadMobilityAuto.class, // 1 speaker | 7 pts
        SourceSide2MidAuto.class, // 3 speaker | 17 pts
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

    public BaseAuto getSelectedAutonomous() {
        try {
            BaseAuto selected = (BaseAuto) autonChooser.getSelected().getDeclaredConstructor().newInstance();
            return selected;
        } catch (Exception e) {
            return null;
        }
    }
}