package frc.robot.modules;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.common.Enums.MatchMode;

public class LoggerModule extends Module {
    private static LoggerModule INSTANCE = null;
    public static LoggerModule getInstance() {
        if (INSTANCE == null) INSTANCE = new LoggerModule();
        return INSTANCE;
    }

    private LoggerModule() {
        Logger.recordMetadata("Crescendo", "MyProject"); // Set a metadata value
        
        if (Robot.isReal()) { // If running on a real robot
            String eventName = DriverStation.getEventName() != ""?DriverStation.getEventName()+"_":"";
            String matchType;
            switch (DriverStation.getMatchType()) {
                default: // Default falls through to None
                case None:
                    matchType = "";
                    break;
                case Practice:
                    matchType = "PRACTICE_";
                    break;
                case Qualification:
                    matchType = "QUALS_";
                    break;
                case Elimination:
                    matchType = "ELIM_";
                    break;
            }
            String matchNumber = DriverStation.getMatchNumber()>0?String.valueOf(DriverStation.getMatchNumber())+"_":"";
            String time = DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
            String path = "/U/"+eventName+matchType+matchNumber+time+".wpilog";
            Logger.addDataReceiver(new WPILOGWriter(path)); // Log to a USB stick
            Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
            Logger.start();
        }
    }

    @Override
    public void init(MatchMode mode) {}

    @Override
    public void initializeLogs() {}

    @Override
    public void periodic() {}
}