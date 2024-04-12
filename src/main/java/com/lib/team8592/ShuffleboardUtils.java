package com.lib.team8592;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShuffleboardUtils {
    private static Dictionary<ShuffleboardTab, ArrayList<String>> allCards = new Hashtable<>();

    public static ShuffleboardTab create(String name) {
        ShuffleboardTab tab = Shuffleboard.getTab(name);
        for (int i = 0; i < allCards.size(); i++) {
            if (tab == allCards.keys().nextElement()) return tab;
        }
        allCards.put(tab, new ArrayList<>());
        return tab;
    }

    public static void add(ShuffleboardTab tab, String key, Object data) {
        ArrayList<String> titles = allCards.get(tab);
        for (String title : titles) {
            if (title == key) return;
        }

        titles.add(key);
        allCards.put(tab, titles);
        tab.add(key, data);
    }
}