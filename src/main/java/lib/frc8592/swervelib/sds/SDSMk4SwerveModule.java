package lib.frc8592.swervelib.sds;

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

    public SDSMk4SwerveModule(Motor driveMotor, Motor steerMotor, SDSConfig moduleConfig, boolean inverted) {
        this.driveMotor = driveMotor;
        this.steerMotor = steerMotor;

        if (inverted && moduleConfig.invertedConfig != null) {
            this.moduleConfig = moduleConfig.invertedConfig;
        } else {
            this.moduleConfig = moduleConfig.regularConfig;
        }
    }
}