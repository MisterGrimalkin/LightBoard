package lightboard.board.impl;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import lightboard.board.ColourSwitcher;
import lightboard.board.RGBLightBoard;
import lightboard.util.ColourNames;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.pi4j.wiringpi.Gpio.OUTPUT;
import static com.pi4j.wiringpi.Gpio.digitalWrite;
import static lightboard.util.ColourNames.*;

/**
 * Second implementation using RaspPi - this one for the Greenpeace field at Glastonbury 2015
 */
public class RaspPiGlastoLightBoard implements RGBLightBoard, ColourSwitcher {

    private final int rows;
    private final int cols;

    // wiringPi pin numbers
    private int clock = 0;
    private int store = 1;
    private int output = 2;
    private int data1R = 3;
    private int data2R = 4;
    private int data1G = 5;
    private int data2G = 6;
    private int addr0 = 21;
    private int addr1 = 22;
    private int addr2 = 23;
    private int addr3 = 24;

    public RaspPiGlastoLightBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void init() {
        System.out.println("Starting Raspberry Pi LightBoard, Greenpeace style....");

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

        digitalWrite(data1R, true);
        digitalWrite(data1G, true);
        digitalWrite(data2R, true);
        digitalWrite(data2G, true);

        System.out.println("Board Ready");
    }

    private static final int MULTI_MODE = 0;
    private static final int RED_MODE = 1;
    private static final int GREEN_MODE = 2;
    private static final int YELLOW_MODE = 3;

    private boolean cycleColours = false;
    private int colour = MULTI_MODE;

    private long t = 0;
    private long colourChangePeriod = 30000;


    /////////////////
    // Binary Data //
    /////////////////

    @Override
    public void dump(boolean[][] data) {
        if ( cycleColours && System.currentTimeMillis()-t > colourChangePeriod) {
            colour++;
            if ( colour> YELLOW_MODE) colour = RED_MODE;
            t = System.currentTimeMillis();
        }
        try {
            for (int row = 0; row < rows/2; row++) {
                sendSerialString(data[row], data[row + (data.length/2)]);
                digitalWrite(output, true);
                decodeRowAddress(row);
                digitalWrite(store, true);
                digitalWrite(store, false);
                digitalWrite(output, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSerialString(boolean[] data1, boolean[] data2) throws InterruptedException {
        for (int col = 0; col < data1.length ; col++) {
            digitalWrite(clock, false);
            if (colour != GREEN_MODE) {
                digitalWrite(data1R, !data1[col]);      // on this board the serial data is inverted
                digitalWrite(data2R, !data2[col]);
            }
            if (colour != RED_MODE) {
                digitalWrite(data1G, !data1[col]);
                digitalWrite(data2G, !data2[col]);
            }
            digitalWrite(clock, true);
        }
    }


    /////////////////
    // Colour Data //
    /////////////////

    @Override
    public void dump(double[][][] data) {
        if ( cycleColours && System.currentTimeMillis()-t > colourChangePeriod) {
            colour++;
            if ( colour> YELLOW_MODE) colour = RED_MODE;
            t = System.currentTimeMillis();
        }
        try {
            for (int row = 0; row < rows/2; row++) {
                sendSerialString(data[0][row], data[1][row], data[0][row+rows/2], data[1][row+rows/2]);
                digitalWrite(output, true);
                decodeRowAddress(row);
                digitalWrite(store, true);
                digitalWrite(store, false);
                digitalWrite(output, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSerialString(double[] red1, double[] green1, double[] red2, double[] green2) throws InterruptedException {
        for (int col = 0; col < red1.length ; col++) {
            digitalWrite(clock, false);
            if ( colour==MULTI_MODE) {
                if ( red1[col] >= 0.5 ) {
                    digitalWrite(data1R, false);
                } else {
                    digitalWrite(data1R, true);
                }
                if ( green1[col] >= 0.5 ) {
                    digitalWrite(data1G, false);
                } else {
                    digitalWrite(data1G, true);
                }
                if ( red2[col] >= 0.5 ) {
                    digitalWrite(data2R, false);
                } else {
                    digitalWrite(data2R, true);
                }
                if ( green2[col] >= 0.5 ) {
                    digitalWrite(data2G, false);
                } else {
                    digitalWrite(data2G, true);
                }
            } else {
                if ( red1[col] >= 0.5 || green1[col] >= 0.5) {
                    if ( colour==GREEN_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data1G, false);
                    }
                    if ( colour==RED_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data1R, false);
                    }
                } else {
                    if ( colour==GREEN_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data1G, true);
                    }
                    if ( colour==RED_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data1R, true);
                    }
                }
                if ( red2[col] >= 0.5 || green2[col] >= 0.5) {
                    if ( colour==GREEN_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data2G, false);
                    }
                    if ( colour==RED_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data2R, false);
                    }
                } else {
                    if ( colour==GREEN_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data2G, true);
                    }
                    if ( colour==RED_MODE || colour==YELLOW_MODE) {
                        digitalWrite(data2R, true);
                    }
                }
            }
            digitalWrite(clock, true);
        }
    }

    private void decodeRowAddress(int row) {
        boolean address0 = BigInteger.valueOf(row).testBit(0);
        boolean address1 = BigInteger.valueOf(row).testBit(1);
        boolean address2 = BigInteger.valueOf(row).testBit(2);
        boolean address3 = BigInteger.valueOf(row).testBit(3);
        if (lastAddress0 ==null || address0!= lastAddress0) {
            lastAddress0 = address0;
            digitalWrite(addr0, address0);
        }
        if (lastAddress1 ==null || address1!= lastAddress1) {
            digitalWrite(addr1, address1);
            lastAddress1 = address1;
        }
        if (lastAddress2 ==null || address2!= lastAddress2) {
            digitalWrite(addr2, address2);
            lastAddress2 = address2;
        }
        if (lastAddress3 ==null || address3!= lastAddress3) {
            digitalWrite(addr3, address3);
            lastAddress3 = address3;
        }
    }

    private Boolean lastAddress0 = null;
    private Boolean lastAddress1 = null;
    private Boolean lastAddress2 = null;
    private Boolean lastAddress3 = null;

    @Override
    public Long getRefreshInterval() {
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
    public List<String> getSupportedColours() {
        List<String> result = new ArrayList<>();
        result.add(RED);
        result.add(GREEN);
        result.add(YELLOW);
        result.add(MULTI);
        return result;
    }

    @Override
    public void setColour(String colourName) {
        if (RED.equals(colourName)) {
            colour = RED_MODE;
            digitalWrite(data1G, true);
            digitalWrite(data2G, true);
        } else if (GREEN.equals(colourName)) {
            colour = GREEN_MODE;
            digitalWrite(data1R, true);
            digitalWrite(data2R, true);
        } else if (YELLOW.equals(colourName)) {
            colour = YELLOW_MODE;
        } else if (MULTI.equals(colourName)) {
            colour = MULTI_MODE;
        }
    }

}
