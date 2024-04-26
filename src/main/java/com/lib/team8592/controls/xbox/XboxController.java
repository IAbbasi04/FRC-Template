package com.lib.team8592.controls.xbox;

import java.lang.reflect.Field;
import java.util.Dictionary;
import java.util.Hashtable;

import com.lib.team8592.ShuffleboardUtils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.common.Constants;
import frc.robot.controls.InputMap;

public class XboxController {
    private Joystick controller;
    private Dictionary<XboxInput, SendableChooser<XboxInput>> inputMap;
    private Dictionary<Field, SendableChooser<XboxInput>> map;
    private String tabName;

    public XboxController(int port, String shuffleboardTabName) {
        this.controller = new Joystick(port);
        inputMap = new Hashtable<>();
        map = new Hashtable<>();
        tabName = shuffleboardTabName;
        Shuffleboard.getTab(shuffleboardTabName);
    }

    /**
     * Gets the [-1, 1] value for an axis input and {0, 1} for buttons; DPAD_RAW returns in degrees [0, 360]
     */
    public double get(XboxInput input) {
        if (Robot.isReal()) {
            return input.get(controller);
        }
        return input.get(new Joystick(0)); // Makes it so that only 1 controller is needed for sim
    }

    /**
     * Determines whether a particular input is currently being pressed or has a non-zero value
     */
    public boolean isPressing(XboxInput input) {
        return Math.abs(input.get(controller)) > Constants.INPUT.PRESSING_AXIS_DEADBAND;
    }

    /**
     * Determines whether any of the inputs are currently being pressed or have a non-zero value
     */
    public boolean isPressingAny(XboxInput ... inputs) {
        for (XboxInput input : inputs) {
            if (Math.abs(input.get(controller)) >= Constants.INPUT.PRESSING_AXIS_DEADBAND) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether all of the inputs are currently being pressed or have a non-zero value
     */
    public boolean isPressingAll(XboxInput ... inputs) {
        for (XboxInput input : inputs) {
            if (Math.abs(input.get(controller)) < Constants.INPUT.PRESSING_AXIS_DEADBAND) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the input has just been pressed (Only works the frame of pressing); Currently only works on buttons
     */
    public boolean getPressed(XboxInput input) {
        return DriverStation.getStickButtonPressed(controller.getPort(), input.getButtonID());
    }

    /**
     * Returns whether the input is no longer being pressed (Only works the frame of releasing); Currently only works on buttons
     */
    public boolean getReleased(XboxInput input) {
        return DriverStation.getStickButtonReleased(controller.getPort(), input.getButtonID());
    }

    /**
     * Shakes the controller at the desired intensity [0, 1]
     */
    public void setRumble(double intensity) {
        controller.setRumble(RumbleType.kBothRumble, intensity);
    }

    /**
     * Gets underlying joystick
     */
    public Joystick getJoystick() {
        return controller;
    }

    /**
     * Logs a particular controller to smart dashboard along with all active values
     */
    public void logButtonStatusToSmartdashboard(String controllerName, boolean activate) {
        if (activate) {
            for (XboxInput input : XboxInput.values()) {
                SmartDashboard.putNumber(controllerName + "/" + input.toString(), input.get(controller));
            }
        }
    }

    /**
     * Logs all inputs for controller into a tab on shuffleboard
     */
    public void logInputsToShuffleboard() {
        ShuffleboardTab tab = ShuffleboardUtils.create(tabName);
        Class<?> cls;
        SendableChooser<XboxInput> inputChooser;
        inputMap = new Hashtable<>();
        map = new Hashtable<>();

        switch (controller.getPort()) {
            case 1:
                cls = InputMap.MANIPULATOR.class;
                break;
            default:
                cls = InputMap.DRIVER.class;
                break;
        }

        for (Field field : cls.getDeclaredFields()) {
            try {
                XboxInput input = ((XboxInput)field.get(null));
                inputChooser = XboxInput.getAsSendable(input);
                inputMap.put(input, inputChooser);
                map.put(field, inputChooser);
                tab.add(field.getName(), inputChooser).withSize(2, 1);
            } catch (Exception e) {}
        }
    }

    /**
     * Updates the controller's desired inputs based on shuffleboard changes
     */
    public void updateChanges() {
        Class<?> cls = InputMap.DRIVER.class;
        if (controller.getPort() == 1) cls = InputMap.MANIPULATOR.class;
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            SendableChooser<XboxInput> chooser = map.get(field);
            XboxInput selected = (XboxInput)chooser.getSelected();
            try {
                field.set(null, selected);
            } catch (Exception e) {}
        }
    }
}