package com.lib.team8592.autonomous;

import edu.wpi.first.math.trajectory.TrajectoryConfig;

public class TrajectoryUtil {
    /**
     * Creates a forward trajectory, given constraints, with a specific start and end velocity
     */
    public static TrajectoryConfig createDefaultTrajectoryConfig(TrajectoryConfig config, double startVel, double endVel) {
        TrajectoryConfig newConfig = new TrajectoryConfig(config.getMaxVelocity(), config.getMaxAcceleration());
        return newConfig.setReversed(false).setStartVelocity(startVel).setEndVelocity(endVel);
    }

    /**
     * Creates a forward trajectory, given constraints
     */
    public static TrajectoryConfig createDefaultTrajectoryConfig(TrajectoryConfig config) {
        return createDefaultTrajectoryConfig(config, config.getStartVelocity(), config.getEndVelocity());
    }

    /**
     * Creates a reversed trajectory, given constraints, with a specific start and end velocity
     */
    public static TrajectoryConfig createReverseTrajectoryConfig(TrajectoryConfig config, double startVel, double endVel) {
        TrajectoryConfig newConfig = new TrajectoryConfig(config.getMaxVelocity(), config.getMaxAcceleration());
        return newConfig.setReversed(true).setStartVelocity(startVel).setEndVelocity(endVel);
    }

    /**
     * Creates a forward trajectory, given constraints
     */
    public static TrajectoryConfig createReverseTrajectoryConfig(TrajectoryConfig config) {
        return createReverseTrajectoryConfig(config, config.getStartVelocity(), config.getEndVelocity());
    }
}