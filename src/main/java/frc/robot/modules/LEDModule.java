package frc.robot.modules;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.Enums.MatchMode;
import frc.robot.hardware.NeoPixelLED;
import frc.robot.hardware.NeoPixelLED.PresetColor;

public class LEDModule extends Module {
    private static LEDModule INSTANCE = null;
    public static LEDModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LEDModule();
        }
        return INSTANCE;
    }

    private NeoPixelLED ledStrip;
    private LEDMode ledMode;

    private Timer ampTimer;
    private Timer stagedNoteTimer;

    public enum LEDMode {
        kOff,
        kDisabled,
        kAmplify,
        kHasNote,
        kStagedNote,
        kTargetLockSearching,
        kTargetLockLocked
    }

    private LEDModule() {
        ledStrip = new NeoPixelLED(0, 8);
        ledMode = LEDMode.kOff;

        ampTimer = new Timer();
        stagedNoteTimer = new Timer();
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

        ampTimer.reset();
        ampTimer.start();

        stagedNoteTimer.reset();
        stagedNoteTimer.start();
    }

    @Override
    public void initializeLogs() {

    }

    @Override
    public void periodic() {
        switch (ledMode) {
            case kAmplify:
                if (ampTimer.get() <= 1.0) {
                    ledStrip.wave(PresetColor.YELLOW, PresetColor.WHITE, 0.05);
                } 
                else {
                    ledStrip.setColor(PresetColor.OFF);
                }
                break;
            case kHasNote:
                ledStrip.pulse(PresetColor.LIME_GREEN, PresetColor.OFF, 0.75);
                break;
            case kStagedNote:
                if (stagedNoteTimer.get() <= 1.0) {
                    ledStrip.scroll(PresetColor.LIME_GREEN, PresetColor.WHITE, 0.05);
                } 
                else {
                    ledStrip.setColor(PresetColor.OFF);
                }
                break;
            case kDisabled:
                ledStrip.scroll(PresetColor.TEAL, PresetColor.ORANGE, 0.05);
                break;
            case kOff:
            default:
                ledStrip.setColor(PresetColor.OFF);
                break;
        }

        if (!ledMode.equals(LEDMode.kAmplify)) ampTimer.reset();
        if (!ledMode.equals(LEDMode.kStagedNote)) stagedNoteTimer.reset();

        SmartDashboard.putString("LED/MODE", ledMode.name());
    }
}