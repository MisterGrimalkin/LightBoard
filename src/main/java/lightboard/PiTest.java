package lightboard;

import com.pi4j.io.gpio.*;

import java.util.Timer;
import java.util.TimerTask;

public class PiTest {

    private static int maxPulse = 25;
    private static int minPulse = 1;
    private static double pulse = minPulse;

    private static double minDelta = 0.5;
    private static double maxDelta = 5.0;
    private static double delta = minDelta;

    private static double deltaDelta = 0.1;

    public static void doThing() throws InterruptedException {

        System.out.println("Starting");

        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        final GpioPinDigitalOutput storePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
        final GpioPinDigitalOutput add0Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        final GpioPinDigitalOutput add1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.HIGH);
        final GpioPinDigitalOutput add2Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
        final GpioPinDigitalOutput add3Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
        final GpioPinDigitalOutput data1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
        final GpioPinDigitalOutput outputEnable1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.HIGH);


        boolean exit = false;

        while ( !exit ) {
            clockPin.high();
            System.out.println("High");
            Thread.sleep(1000);
            clockPin.low();
            System.out.println("Low");
            Thread.sleep(1000);

            boolean set = Math.random() > 0.5;
            System.out.println("Putting " + set);
            if ( set ) {
                data1Pin.high();
            } else {
                data1Pin.low();
            }
//            clockPin.high();
//            Thread.sleep(100);
//            clockPin.low();
//            Thread.sleep(100);
//            storePin.high();
//            Thread.sleep(100);
//            storePin.low();
//            Thread.sleep(100);
        }

        System.out.println("Done");

    }



    public static void startPulse() throws InterruptedException {

        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
//        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        pin.setShutdownOptions(true, PinState.LOW);
        pin.low();

        System.out.println("Start:");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                pin.pulse((int) Math.floor(pulse), true);
                pulse += delta;
                if (pulse >= maxPulse) {
                    // Max pulse
                    delta = -delta;
                    pulse = maxPulse;
                } else if (pulse <= minPulse) {
                    // Min pulse
                    delta = -delta;
                    pulse = minPulse;
                    // Update delta
                    delta += deltaDelta;
                    if ( delta >= maxDelta ) {
                        delta = maxDelta;
                        deltaDelta = -deltaDelta;
                    } else if ( delta <= minDelta ) {
                        delta = minDelta;
                        deltaDelta = -deltaDelta;
                    }
                }
            }
        }, 0, maxPulse);

    }

}
