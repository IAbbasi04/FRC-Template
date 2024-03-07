package frc.robot.hardware;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    private NetworkTable limelightTable; // Network table associated with the given limelight name

    public Limelight(String limelightName) {
        limelightTable = NetworkTableInstance.getDefault().getTable(limelightName);
    }

    /**
     * The limelight's horizontal angle to the target
     */
    public double getX() {
        return limelightTable.getEntry("tx").getDouble(0.0);
    }

    /**
     * The limelight's vertical angle to the target
     */
    public double getY() {
        return limelightTable.getEntry("ty").getDouble(0.0);
    }

    /**
     * If there is a target visible in the limelight
     */
    public boolean isTargetValid() {
        return limelightTable.getEntry("tv").getDouble(0.0) == 1.0;
    }

    /**
     * Sets the visible pipeline of the limelight
     */
    public void setPipeline(double pipeline) {
        limelightTable.getEntry("pipeline").setDouble(pipeline);
    }

    /**
     * Returns the current visible pipeline number of the limelight
     */
    public int getPipeline() {
        return (int)limelightTable.getEntry("pipeline").getDouble(0);
    }
}