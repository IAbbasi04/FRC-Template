package lib.frc8592.logging;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.Robot;

public class SmartLogger {
    private String tab;
    private Dictionary<String, LoggerEntry<?>> data;
    private Dictionary<String, GenericEntry> cards;

    private boolean logToShuffleboard = true;
    
    public SmartLogger(String tab) {
        this.tab = tab;
        data = new Hashtable<>();
        cards = new Hashtable<>();
    }

    public SmartLogger enable() {
        this.logToShuffleboard = true;
        return this;
    }

    public SmartLogger disable() {
        this.logToShuffleboard = Robot.isSimulation();
        return this;
    }

    public void logDouble(String key, Supplier<Double> value) {
        this.data.put(key, new LoggerEntry<Double>(value));
        if (!this.logToShuffleboard) return; // Do not proceed if we do not want to log to shuffleboard

        if (cards.get(key) == null) { // Card doesn't exist yet on shuffleboard
            cards.put(key, Shuffleboard.getTab(tab).add(key, value.get()).getEntry());
        } else { // Card already exists on shuffleboard
            cards.get(key).setDouble(value.get());
        }
    }

    public void logBoolean(String key, Supplier<Boolean> value) {
        this.data.put(key, new LoggerEntry<Boolean>(value));
        if (!this.logToShuffleboard) return; // Do not proceed if we do not want to log to shuffleboard

        if (cards.get(key) == null) { // Card doesn't exist yet on shuffleboard
            cards.put(key, Shuffleboard.getTab(tab).add(key, value.get()).getEntry());
        } else { // Card already exists on shuffleboard
            cards.get(key).setBoolean(value.get());
        }
    }

    public <T extends Enum<T>> void logEnum(String key, Supplier<T> value) {
        this.data.put(key, new LoggerEntry<T>(value));
        if (!this.logToShuffleboard) return; // Do not proceed if we do not want to log to shuffleboard

        if (cards.get(key) == null) { // Card doesn't exist yet on shuffleboard
            cards.put(key, Shuffleboard.getTab(tab).add(key, value.get().name()).getEntry());
        } else { // Card already exists on shuffleboard
            cards.get(key).setString(value.get().name());
        }
    }

    public <T> void logData(String key, Supplier<T> value) {
        this.data.put(key, new LoggerEntry<T>(value));
        if (!this.logToShuffleboard) return; // Do not proceed if we do not want to log to shuffleboard

        if (cards.get(key) == null) { // Card doesn't exist yet on shuffleboard
            cards.put(key, Shuffleboard.getTab(tab).add(key, value.get().toString()).getEntry());
        } else { // Card already exists on shuffleboard
            cards.get(key).setString(value.get().toString());
        }
    }

    public void update() {
        for (String key : Collections.list(data.keys())) { // Update every logged value
            if (data.get(key).getDataClass() == Double.class) { // Double
                Logger.recordOutput(this.tab + "/" + key, data.get(key).getDouble());
                if (cards.get(key) != null) {
                    cards.get(key).setDouble(data.get(key).getDouble());
                }
            } else if (data.get(key).getDataClass() == Boolean.class) { // Boolean
                Logger.recordOutput(this.tab + "/" + key, data.get(key).getBoolean());
                if (cards.get(key) != null) {
                    cards.get(key).setBoolean(data.get(key).getBoolean());
                }
            } else if (data.get(key).getDataClass().getEnumConstants() != null) { // Enum Value
                Logger.recordOutput(this.tab + "/" + key, data.get(key).getEnumName());
                if (cards.get(key) != null) {
                    cards.get(key).setString(data.get(key).getEnumName());
                }
            } else { // Other type
                Logger.recordOutput(this.tab + "/" + key, data.get(key).getString());
                if (cards.get(key) != null) {
                    cards.get(key).setString(data.get(key).getString());
                }
            }
        }
    }
}