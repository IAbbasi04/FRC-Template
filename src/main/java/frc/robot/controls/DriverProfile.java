package frc.robot.controls;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.common.Constants;

public enum DriverProfile {
    DEFAULT, // Note - Default has to exist in order to maintain a non-driver specific profile
    IBRAHIM,
    ;

    private static ShuffleboardTab tab = Shuffleboard.getTab(Constants.INPUT.DRIVER_SHUFFLEBOARD_TAB);
    private static SendableChooser<DriverProfile> chooser = new SendableChooser<>();
    private static Dictionary<Field, EXboxController> originalMappings = new Hashtable<>();
    private static boolean hasLogged = false;
    private static DriverProfile profile = DriverProfile.DEFAULT;

    /**
     * Puts the driver selector into Smartdashboard
     */
    public static void logToSmartdashboard() {
        if (hasLogged) return;
        chooser.setDefaultOption("Default", DEFAULT);
        for (DriverProfile driver : DriverProfile.values()) {
            if (driver != DEFAULT) {
                chooser.addOption(driver.name(), driver);
            }
        }

        tab.add("Select Driver", chooser).withPosition(4, 3).withSize(2, 1);
        hasLogged = true;
    }

    /**
     * Logs selected driver to smartdashboard
     */
    public static void updateSelected() {
        profile = (DriverProfile) chooser.getSelected();
    }

    /**
     * Selected driver profile
     */
    public static DriverProfile getSelected() {
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
     * Updates inputs to be driver specific
     */
    public static void changeInputs() {
        // Inputs changed in the following format:
        // [InputMap.DRIVER.*INPUT* = updatedInput("*INPUT*", *NEW CONTROL*)]
        DriverProfile selected = (DriverProfile) chooser.getSelected();
        switch(selected) {
            case IBRAHIM:
                InputMap.DRIVER.SNAIL_MODE = updatedInput("SNAIL_MODE", EXboxController.LEFT_BUMPER);
                InputMap.DRIVER.RESET_GYRO = updatedInput("RESET_GYRO", EXboxController.START);
                break;
            default: // Nothing should change
                resetInputsToDefault();
                break;
        }
    }
}