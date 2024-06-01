package frc.robot.subsystems;

import lib.frc8592.MatchMode;
import lib.frc8592.hardware.NeoPixelLED;
import lib.frc8592.hardware.NeoPixelLED.PresetColor;
import lib.frc8592.logging.SmartLogger;

public class LEDSubsystem extends Subsystem {
    private static LEDSubsystem INSTANCE = null;
    public static LEDSubsystem getInstance() {
        if (INSTANCE == null) INSTANCE = new LEDSubsystem();
        return INSTANCE;
    }

    private NeoPixelLED ledStrip;
    private LEDMode ledMode;

    public enum LEDMode {
        kOff,
        kDisabled
    }

    private LEDSubsystem() {
        ledStrip = new NeoPixelLED(0, 8);
        ledMode = LEDMode.kOff;

        super.logger = new SmartLogger("LEDSubsystem");
    }
    
    public void setLEDMode(LEDMode ledMode) {
        this.ledMode = ledMode;
    }

    @Override
    public void init(MatchMode mode) {
        if (mode == MatchMode.DISABLED) {
            ledMode = LEDMode.kDisabled;
        } else {
            ledMode = LEDMode.kOff;
        }
    }

    @Override
    public void initializeLogs() {
        logger.logEnum("LED Mode", () -> ledMode);
    }

    @Override
    public void periodic() {
        switch (ledMode) {
            case kDisabled:
                ledStrip.scroll(PresetColor.TEAL, PresetColor.ORANGE, 0.05);
                break;
            case kOff: // Fall through intentional
            default:
                ledStrip.setColor(PresetColor.OFF);
                break;
        }
    }
}