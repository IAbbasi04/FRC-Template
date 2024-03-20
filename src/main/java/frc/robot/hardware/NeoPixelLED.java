package frc.robot.hardware;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;

public class NeoPixelLED {
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;
    private final int LED_LENGTH;

    private Timer pulseTimer;
    private Timer scrollTimer;
    private Timer waveTimer;

    private int scrollIndex = 0;
    private int waveIndex = 0;

    public enum PresetColor {
        OFF(0, 0, 0),
        RED(255, 0, 0),
        BLUE(0, 0, 255),
        ORANGE(255, 50, 50),
        LIME_GREEN(100, 250, 200),
        TEAL(0, 255, 255),
        YELLOW(255, 255, 0),
        GREEN(0, 255, 0),
        PURPLE(255, 0, 255),
        WHITE(255, 255, 255);

        public int r, g, b;
        private PresetColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    public NeoPixelLED(int port, int length) {
        led = new AddressableLED(port);
        led.setLength(length);
        ledBuffer = new AddressableLEDBuffer(length);
        led.start();
        LED_LENGTH = length;

        pulseTimer = new Timer();
        scrollTimer = new Timer();
        waveTimer = new Timer();

        pulseTimer.reset();
        pulseTimer.start();

        scrollTimer.reset();
        scrollTimer.start();

        waveTimer.reset();
        waveTimer.start();
    }

    public void pulse(PresetColor color1, PresetColor color2, double secondsPerPulse) {
        if (pulseTimer.get() < secondsPerPulse / 2.0) {
            this.setColor(color1);
        } else if (pulseTimer.get() < secondsPerPulse) {
            this.setColor(color2);
        } else {
            pulseTimer.reset();
        }
    }

    public void scroll(PresetColor color1, PresetColor color2, double scrollTime) {
        if (scrollTimer.get() >= scrollTime) {
            scrollIndex++;
            scrollTimer.reset();
        }

        for (int i = 0; i < LED_LENGTH; i++) {
            if (Math.sin(i + scrollIndex) > 0){
                setColor(i, color2);
            }
            else  {
                setColor(i, color1);
            }
        }
    }

    public void wave(PresetColor color1, PresetColor color2, double waveTime) {
        if (waveTimer.get() >= waveTime) {
            waveIndex++;
            waveTimer.reset();
        }

        for (int i = 0; i < LED_LENGTH / 2; i++) {
            if (Math.sin(i + waveIndex) > 0){
                setColor(i, color2);
                setColor(LED_LENGTH - i - 1, color2);
            }
            else  {
                setColor(i, color1);
                setColor(LED_LENGTH - i - 1, color1);
            }
        }
    }

    public void setColor(int pixel, int brightness, PresetColor color) {
        ledBuffer.setRGB(pixel, brightness*color.r, brightness*color.g, brightness*color.b);
        led.setData(ledBuffer);
    }

    public void setColor(int pixel, PresetColor color) {
        this.setColor(pixel, 1, color);
    }

    public void setColor(PresetColor color) {
        for (int i = 0; i < LED_LENGTH; i++) {
            this.setColor(i, color);
        }
    }
}