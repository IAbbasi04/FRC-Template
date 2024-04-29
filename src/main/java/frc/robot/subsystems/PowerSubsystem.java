package frc.robot.subsystems;

import java.util.ArrayList;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.Constants.POWER;
import lib.frc8592.MatchMode;
import frc.robot.common.Ports;

public class PowerSubsystem extends Subsystem {
    private static PowerSubsystem INSTANCE = null;
    public static PowerSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new PowerSubsystem();
        return INSTANCE;
    }

    // Low battery warning
    private double currentBatteryVoltage;
    private ArrayList<Double> voltages;
    private boolean isBatteryLow = false;

    // Object variables
    private PowerDistribution revPDH;

    //
    // Shared power variables
    //
    public double temp;
    public double voltage;
    public double current;
    public double power;
    public double energy;

    private PowerSubsystem() {
        revPDH = new PowerDistribution(Ports.PDH_CAN_ID, PowerDistribution.ModuleType.kRev);
        super.addLogger("Power", true); // Always log power status
        voltages = new ArrayList<Double>();
    }

    @Override
    public void init(MatchMode mode) {

    }

    @Override
    public void initializeLogs() {

    }

    @Override
    public void periodic() {
        current = revPDH.getTotalCurrent();
        // Get parameters from the PDH
        
        temp    = revPDH.getTemperature();
        voltage = revPDH.getVoltage();
        power   = revPDH.getTotalPower();
        energy  = revPDH.getTotalEnergy();

        voltages.add(0, voltage);
        if (voltages.size() > POWER.VOLTAGE_SMOOTHING_LENGTH) {
            voltages.remove(POWER.VOLTAGE_SMOOTHING_LENGTH);
        }
        double x = 0.0;
        for (double i : voltages) {
            x += i;
        }
        currentBatteryVoltage = x / POWER.VOLTAGE_SMOOTHING_LENGTH;
        if (DriverStation.isDisabled() || DriverStation.isAutonomous() || DriverStation.isTest()) {
            if (currentBatteryVoltage < POWER.DISABLED_LOW_BATTERY_VOLTAGE) {
                isBatteryLow = true;
            }
        }

        if (DriverStation.isTeleop()) {
            if (currentBatteryVoltage < POWER.TELEOP_LOW_BATTERY_VOLTAGE) {
                isBatteryLow = true;
            }
        }

        Logger.recordOutput(POWER.LOG_PATH + "Is Battery Low", isBatteryLow);
        SmartDashboard.putBoolean("Is Battery Low", isBatteryLow);
    }
}