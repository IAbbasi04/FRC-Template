package lib.frc8592.swervelib.sds;

import com.ctre.phoenix6.hardware.CANcoder;

import frc.robot.common.Constants;
import lib.frc8592.hardware.motors.Motor;
import lib.frc8592.swervelib.ModuleConfiguration;
import lib.frc8592.swervelib.SwerveModule;

public class SDSMk4SwerveModule extends SwerveModule {
    public enum SDSConfig {
        Mk4L1(SdsModuleConfigurations.MK4_L1, SdsModuleConfigurations.MK4I_L1),
        Mk4L2(SdsModuleConfigurations.MK4_L2, SdsModuleConfigurations.MK4I_L2),
        Mk4L3(SdsModuleConfigurations.MK4_L3, SdsModuleConfigurations.MK4I_L3),
        Mk4L4(SdsModuleConfigurations.MK4_L4, null), // Inverted Mk4 L4 gearing does not exist
        ;

        public ModuleConfiguration regularConfig, invertedConfig;
        private SDSConfig(ModuleConfiguration regular, ModuleConfiguration inverted) {
            this.regularConfig = regular;
            this.invertedConfig = inverted;
        }
    }

    public SDSMk4SwerveModule(Motor driveMotor, Motor steerMotor, SDSConstants constants) {
        this.driveMotor = driveMotor;
        this.steerMotor = steerMotor;
        if (constants.inverted && constants.moduleConfig.invertedConfig != null) {
            this.moduleConfig = constants.moduleConfig.invertedConfig;
        } else {
            this.moduleConfig = constants.moduleConfig.regularConfig;
        }

        this.driveMotor.withGains(Constants.SWERVE.THROTTLE_GAINS, 0);
        this.steerMotor.withGains(Constants.SWERVE.STEER_GAINS, 0);

        this.cancoder = new CANcoder(constants.cancoderID);
    }

    public static class SDSConstants {
        public final SDSConfig moduleConfig;
        public final int cancoderID;
        public final boolean inverted;
        public final double steerOffset;

        public SDSConstants(SDSConfig moduleConfig, int cancoderID, boolean inverted, double steerOffset) {
            this.moduleConfig = moduleConfig;
            this.cancoderID = cancoderID;
            this.inverted = inverted;
            this.steerOffset = steerOffset;
        }

    }
}