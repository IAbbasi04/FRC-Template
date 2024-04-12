package com.lib.team8592.logging;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Supplier;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SmartLogger {
    private ShuffleboardTab tab;
    private Dictionary<String, Supplier<?>> tabData;
    private Dictionary<String, GenericEntry> cards;

    private Dictionary<String, LoggerEntry<?>> data;

    private boolean logToShuffleboard;

    public SmartLogger(String tabName, boolean logToShuffleboard) {
        this.tab = Shuffleboard.getTab(tabName);
        this.tabData = new Hashtable<>();
        this.cards = new Hashtable<>();
        this.data = new Hashtable<>();

        this.logToShuffleboard = logToShuffleboard;
    }

    public void setBoolean(String cardName, Supplier<Boolean> suppliedBoolean) {
        tabData.put(cardName, suppliedBoolean);
        data.put(cardName, new LoggerEntry<Boolean>(suppliedBoolean));
        if (!logToShuffleboard) return;

        if (cards.get(cardName) == null) {
            cards.put(cardName, tab.add(cardName, suppliedBoolean.get()).getEntry());
        } else {
            cards.get(cardName).setBoolean(suppliedBoolean.get());
        }
    }

    public boolean getBoolean(String cardName) {
        return data.get(cardName).getBoolean();
    }

    public void setNumber(String cardName, Supplier<Double> suppliedNumber) {
        tabData.put(cardName, suppliedNumber);
        data.put(cardName, new LoggerEntry<Double>(suppliedNumber));
        if (!logToShuffleboard) return;
        if (cards.get(cardName) == null) {
            cards.put(cardName, tab.add(cardName, suppliedNumber.get()).getEntry());
        } else {
            cards.get(cardName).setDouble(suppliedNumber.get());
        }
    }

    public double getNumber(String cardName) {
        return data.get(cardName).getDouble();
    }

    public void setString(String cardName, Supplier<String> suppliedString) {
        tabData.put(cardName, suppliedString);
        data.put(cardName, new LoggerEntry<String>(suppliedString));
        if (!logToShuffleboard) return;
        if (cards.get(cardName) == null) {
            cards.put(cardName, tab.add(cardName, suppliedString.get()).getEntry());
        } else {
            cards.get(cardName).setString(suppliedString.get());
        }
    }

    public String getString(String cardName) {
        return data.get(cardName).getString();
    }

    public <T> void setData(String cardName, Supplier<T> suppliedData) {
        tabData.put(cardName, suppliedData);
        data.put(cardName, new LoggerEntry<T>(suppliedData));
        if (!logToShuffleboard) return;
        if (cards.get(cardName) == null) {
            cards.put(cardName, tab.add(cardName, suppliedData.get().toString()).getEntry());
        } else {
            cards.get(cardName).setString(suppliedData.get().toString());
        }
    }

    public <T> T getData(String cardName) {
        return (T) data.get(cardName);
    }

    public <T extends Enum<T>> void setEnum(String cardName, Supplier<T> suppliedEnum) {
        tabData.put(cardName, suppliedEnum);
        data.put(cardName, new LoggerEntry<T>(suppliedEnum));
        if (!logToShuffleboard) return;
        if (cards.get(cardName) == null) {
            cards.put(cardName, tab.add(cardName, suppliedEnum.get().name()).getEntry());
        } else {
            cards.get(cardName).setString(suppliedEnum.get().name());
        }
    }

    public <T extends Enum<T>> T getEnum(String cardName) {
        T[] enumConstants = (T[]) data.get(cardName).getDataClass().getEnumConstants();
        return enumConstants[data.get(cardName).getEnumOrdinal()];
    }

    public void update() {
        if (!logToShuffleboard) return;
        for (String key : Collections.list(data.keys())) {
            if (data.get(key).getDataClass() == Double.class) {
                cards.get(key).setDouble(data.get(key).getDouble());
            } else if (data.get(key).getDataClass() == Boolean.class) {
                cards.get(key).setBoolean(data.get(key).getBoolean());
            } else if (data.get(key).getDataClass().getEnumConstants() != null) {
                cards.get(key).setString(data.get(key).getEnumName());
            }
        }
    }
}