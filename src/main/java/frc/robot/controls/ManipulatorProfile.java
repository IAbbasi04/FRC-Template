package frc.robot.controls;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.common.Constants;

public enum ManipulatorProfile {
    DEFAULT, // Note - Default has to exist in order to maintain a non-driver specific profile
    ;

    private static ShuffleboardTab tab = Shuffleboard.getTab(Constants.INPUT.MANIPULATOR_SHUFFLEBOARD_TAB);
    private static SendableChooser<ManipulatorProfile> chooser = new SendableChooser<>();
    private static Dictionary<Field, EXboxController> originalMappings = new Hashtable<>();
    private static boolean hasLogged = false;
    private static ManipulatorProfile profile = ManipulatorProfile.DEFAULT;

    /**
     * Puts the driver selector into Smartdashboard
     */
    public static void logToSmartdashboard() {
        if (hasLogged) return;
        chooser.setDefaultOption("Default", DEFAULT);
        for (ManipulatorProfile manipulator : ManipulatorProfile.values()) {
            if (manipulator != DEFAULT) {
                chooser.addOption(manipulator.name(), manipulator);
            }
        }

        tab.add("Select Manipulator", chooser).withPosition(4, 3).withSize(2, 1);
        hasLogged = true;
    }

    /**
     * Logs selected manipulator to smartdashboard
     */
    public static void updateSelected() {
        profile = (ManipulatorProfile) chooser.getSelected();
    }

    /**
     * Selected manipulator profile
     */
    public static ManipulatorProfile getSelected() {
        return profile;
    }

    /**
     * Updates a single input and adds original button to original mappings list
     */
    public static EXboxController updatedInput(String input, EXboxController control) {
        try {
            Field inputField = InputMap.MANIPULATOR.class.getDeclaredField(input);
            EXboxController ogValue = (EXboxController) inputField.get(null);
            originalMappings.put(inputField, ogValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return control;
    }

    /**
     * Resets all controls back to their original
     */
    private static void resetInputsToDefault() {
        for (Field field : Collections.list(originalMappings.keys())) {
            try {
                field.set(null, originalMappings.get(field));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates inputs to be manipulator specific
     */
    public static void changeInputs() {
        // Inputs changed in the following format:
        // [InputMap.OPERATOR.*INPUT* = updatedInput("*INPUT*", *NEW CONTROL*)]
        ManipulatorProfile selected = (ManipulatorProfile) chooser.getSelected();
        switch(selected) {
            default: // Nothing should change
                resetInputsToDefault();
                break;
        }
    }
}