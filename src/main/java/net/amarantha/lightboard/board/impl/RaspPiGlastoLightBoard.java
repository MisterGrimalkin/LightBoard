package net.amarantha.lightboard.board.impl;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import net.amarantha.lightboard.board.ColourSwitcher;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.module.Cols;
import net.amarantha.lightboard.module.Rows;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.pi4j.wiringpi.Gpio.digitalWrite;
import static net.amarantha.lightboard.entity.Colour.*;

/**
 * Second implementation using RaspPi - this one for the Greenpeace field at Glastonbury 2015
 */
public class RaspPiGlastoLightBoard implements LightBoard, ColourSwitcher {

    private int rows;
    private int cols;

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

    private double[][][] currentFrame;
    private double[][][] nextFrame;

    private void pushTestPattern() {
        currentFrame = new double[3][rows][cols];
        nextFrame = currentFrame;
        for ( int r=0; r<rows; r++ ) {
            for ( int c=0; c<cols; c++ ) {
                if ( c%4==0 || r%4==0 ) {
                    currentFrame[0][r][c] = 1.0;
                    currentFrame[1][r][c] = 1.0;
                    currentFrame[2][r][c] = 1.0;
                }
            }
        }
    }

    @Override
    public void init(int rows, int cols) {
        System.out.println("Starting Raspberry Pi LightBoard, Greenpeace style....");

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

        digitalWrite(data1R, true);
        digitalWrite(data1G, true);
        digitalWrite(data2R, true);
        digitalWrite(data2G, true);

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

    private static final int MULTI_MODE = 0;
    private static final int RED_MODE = 1;
    private static final int GREEN_MODE = 2;
    private static final int YELLOW_MODE = 3;

    private int colour = MULTI_MODE;

    private boolean sleeping = false;

    @Override
    public void update(double[][][] data) {
        nextFrame = new double[3][rows][cols];
        for ( int r=0; r<rows; r++ ) {
            for ( int c=0; c<cols; c++ ) {
                nextFrame[0][r][c] = data[0][r][c];
                nextFrame[1][r][c] = data[1][r][c];
                nextFrame[2][r][c] = data[2][r][c];
            }
        }
    }

    public void push() {
        currentFrame = nextFrame;
        try {
            for (int row = 0; row < rows/2; row++) {
                double[][] redFrame = currentFrame[0];
                double[][] greenFrame = currentFrame[1];
                sendSerialString(redFrame[row], greenFrame[row], redFrame[row+rows/2], greenFrame[row+rows/2]);
                digitalWrite(output, true);
                decodeRowAddress(row);
                digitalWrite(store, false);
                digitalWrite(store, true);
                digitalWrite(output, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean lastRed1 = null;
    private Boolean lastGreen1 = null;
    private Boolean lastRed2 = null;
    private Boolean lastGreen2 = null;

    private void sendSerialString(double[] red1, double[] green1, double[] red2, double[] green2) throws InterruptedException {
        for (int col = 0; col < cols ; col++) {
            digitalWrite(clock, false);
            if ( colour==MULTI_MODE) {
                if ( lastRed1==null || lastRed1!=red1[col]<0.5 ) {
                    digitalWrite(data1R, lastRed1=red1[col]<0.5 );
                }
                if ( lastGreen1==null || lastGreen1!=green1[col]<0.5 ) {
                    digitalWrite(data1G, lastGreen1=green1[col]<0.5 );
                }
                if ( lastRed2==null || lastRed2!=red2[col]<0.5 ) {
                    digitalWrite(data2R, lastRed2=red2[col]<0.5 );
                }
                if ( lastGreen2==null || lastGreen2!=green2[col]<0.5 ) {
                    digitalWrite(data2G, lastGreen2=green2[col]<0.5 );
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
            digitalWrite(addr0, lastAddress0 = address0);
        }
        if (lastAddress1 ==null || address1!= lastAddress1) {
            digitalWrite(addr1, lastAddress1 = address1);
        }
        if (lastAddress2 ==null || address2!= lastAddress2) {
            digitalWrite(addr2, lastAddress2 = address2);
        }
        if (lastAddress3 ==null || address3!= lastAddress3) {
            digitalWrite(addr3, lastAddress3 = address3);
        }
    }

    private Boolean lastAddress0 = null;
    private Boolean lastAddress1 = null;
    private Boolean lastAddress2 = null;
    private Boolean lastAddress3 = null;

    @Override
    public Long getUpdateInterval() {
        return 10L;
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
    }

    @Override
    public void wake() {
        sleeping = false;
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

    private String colourName = "multi";

    @Override
    public String getColour() {
        return colourName;
    }

    @Override
    public void setColour(String colourName) {
        this.colourName = colourName;
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
