package net.amarantha.lightboard.board;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import static com.pi4j.wiringpi.Gpio.digitalWrite;
import static java.math.BigInteger.valueOf;

/**
 * Java-driven Raspberry Pi LightBoard
 */
public class JavaLightBoard implements LightBoard {

    private int rows;
    private int cols;

    // wiringPi pin numbers
    private static final int CLOCK = 0;
    private static final int STORE = 1;
    private static final int OUTPUT = 2;
    private static final int DATA_RED_1 = 3;
    private static final int DATA_RED_2 = 4;
    private static final int DATA_GRN_1 = 5;
    private static final int DATA_GRN_2 = 6;
    private static final int ADDRESS_0 = 21;
    private static final int ADDRESS_1 = 22;
    private static final int ADDRESS_2 = 23;
    private static final int ADDRESS_3 = 24;

    private boolean[] pinStates = new boolean[25]; // Must be highest pin number + 1

    private double[][][] currentFrame;
    private double[][][] nextFrame;

    private void setPin(int pin, boolean state) {
        if ( pinStates[pin] != state ) {
            pinStates[pin] = state;
            digitalWrite(pin, state);
        }
    }

    @Override
    public void init(int rows, int cols) {

        System.out.println("Starting Raspberry Pi Java LightBoard....");

        this.rows = rows;
        this.cols = cols;
        pushTestPattern();

        // IMPORTANT:
        // It is necessary to set the pins up via the GpioFactory so that subsequent calls
        // via the Gpio static methods use the wiringPi pin numbers

        GpioController gpio = GpioFactory.getInstance();
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, PinState.LOW);
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW);

        setPin(DATA_RED_1, true);
        setPin(DATA_GRN_1, true);
        setPin(DATA_RED_2, true);
        setPin(DATA_GRN_2, true);

        Thread t = new Thread(() -> {
            while(true) {
                if ( !sleeping ) {
                    push();
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();

        System.out.println("Board Ready");
    }

    private boolean sleeping = false;

    @Override
    public void update(double[][][] data) {
        nextFrame = data;
    }

    public void push() {
        currentFrame = nextFrame;
        try {
            for (int row = 0; row < rows/2; row++) {
                double[][] redFrame = currentFrame[0];
                double[][] greenFrame = currentFrame[1];
                sendSerialString(redFrame[row], greenFrame[row], redFrame[row+rows/2], greenFrame[row+rows/2]);
                setPin(OUTPUT, true);
                setPin(STORE, false);
                decodeRowAddress(row);
                setPin(STORE, true);
                setPin(OUTPUT, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSerialString(double[] red1, double[] green1, double[] red2, double[] green2) throws InterruptedException {
        for (int col = 0; col < cols ; col++) {
            setPin(CLOCK, false);
            setPin(DATA_RED_1, red1[col]<0.5 );
            setPin(DATA_GRN_1, green1[col]<0.5 );
            setPin(DATA_RED_2, red2[col]<0.5 );
            setPin(DATA_GRN_2, green2[col]<0.5 );
            setPin(CLOCK, true);
        }
    }

    private void decodeRowAddress(int row) {
        setPin(ADDRESS_0, valueOf(row).testBit(0));
        setPin(ADDRESS_1, valueOf(row).testBit(1));
        setPin(ADDRESS_2, valueOf(row).testBit(2));
        setPin(ADDRESS_3, valueOf(row).testBit(3));
    }

    @Override
    public Long getUpdateInterval() {
        return null;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public void sleep() {
        sleeping = true;
        update(new double[currentFrame.length][currentFrame[0].length][currentFrame[0][0].length]);
        push();
    }

    @Override
    public void wake() {
        sleeping = false;
    }

    private void pushTestPattern() {
        currentFrame = new double[3][rows][cols];
        nextFrame = currentFrame;
        for ( int r=0; r<rows; r++ ) {
            for ( int c=0; c<cols; c++ ) {
                if ( (c+2)%4==0 || (r+2)%4==0 ) {
                    currentFrame[0][r][c] = 1.0;
                    currentFrame[1][r][c] = 1.0;
                    currentFrame[2][r][c] = 1.0;
                }
            }
        }
    }

}
