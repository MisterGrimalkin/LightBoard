package lightboard.board.impl;

import com.pi4j.io.gpio.*;
import lightboard.board.HasColourSwitcher;
import lightboard.board.LightBoard;

import java.math.BigInteger;

public class RaspberryPiLightBoard2 implements LightBoard, HasColourSwitcher {

    private int rows = 16;
    private int cols = 180;

    private GpioPinDigitalOutput clockPin;
    private GpioPinDigitalOutput storePin;
    private GpioPinDigitalOutput outputPin;
    private GpioPinDigitalOutput data1PinR;
    private GpioPinDigitalOutput data1PinG;
    private GpioPinDigitalOutput data2PinR;
    private GpioPinDigitalOutput data2PinG;
    private GpioPinDigitalOutput address0Pin;
    private GpioPinDigitalOutput address1Pin;
    private GpioPinDigitalOutput address2Pin;
    private GpioPinDigitalOutput address3Pin;
    private GpioPinDigitalOutput outputEnable1Pin;
    private GpioPinDigitalOutput outputEnable2Pin;

    public RaspberryPiLightBoard2(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void init() {
        System.out.println("Starting Raspberry Pi LightBoard, Greenpeace style....");
        GpioController gpio = GpioFactory.getInstance();
        clockPin =  gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        storePin =  gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
        outputPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        data1PinR = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        data2PinR = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
        data1PinG = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
        data2PinG = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
        address0Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW);
        address1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, PinState.LOW);
        address2Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, PinState.LOW);
        address3Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW);
//        address0Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A0, PinState.LOW);
//        address1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A1, PinState.LOW);
//        address2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A2, PinState.LOW);
//        address3Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A3, PinState.LOW);
//        outputEnable1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A4, PinState.HIGH);
//        outputEnable2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A5, PinState.HIGH);
        data1PinR.low();
        data2PinR.low();
        System.out.println("Board Ready");
    }

    private static final int RED = 1;
    private static final int GREEN = 2;
    private static final int YELLOW = 3;

    private boolean cycleColours = false;
    private int colour = RED;

    private long t = 0;
    private long colourChangePeriod = 30000;

    @Override
    public void dump(boolean[][] data) {
        if ( cycleColours && System.currentTimeMillis()-t > colourChangePeriod) {
            colour++;
            if ( colour> YELLOW) colour = RED;
            t = System.currentTimeMillis();
        }
        try {
            for (int row = 0; row < data.length/2; row++) {
                sendSerialString(data[row], data[row+(data.length/2)]);
                outputPin.high();
                decodeRowAddress(row);
                storePin.high();
                storePin.low();
                outputPin.low();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSerialString(boolean[] data1, boolean[] data2) throws InterruptedException {
//        Boolean lastValue1 = null;
//        Boolean lastValue2 = null;
        for (int col = data1.length-1; col >= 0; col--) {
            clockPin.low();
            if (data1[col]) {
                if ( colour!= GREEN) data1PinR.high();
                if ( colour!= RED) data1PinG.high();
            } else {
                if ( colour!= GREEN) data1PinR.low();
                if ( colour!= RED) data1PinG.low();
            }
            if (data2[col]) {
                if ( colour!= GREEN) data2PinR.high();
                if ( colour!= RED) data2PinG.high();
            } else {
                if ( colour!= GREEN) data2PinR.low();
                if ( colour!= RED) data2PinG.low();
            }
            clockPin.high();
        }
    }

    private void decodeRowAddress(int row) {
        boolean address0 = BigInteger.valueOf(row).testBit(0);
        boolean address1 = BigInteger.valueOf(row).testBit(1);
        boolean address2 = BigInteger.valueOf(row).testBit(2);
        boolean address3 = BigInteger.valueOf(row).testBit(3);
        if (lastAddress0 ==null || address0!= lastAddress0) {
            lastAddress0 = address0;
            if (address0) {
                address0Pin.high();
            } else {
                address0Pin.low();
            }
        }
        if (lastAddress1 ==null || address1!= lastAddress1) {
            lastAddress1 = address1;
            if (address1) {
                address1Pin.high();
            } else {
                address1Pin.low();
            }
        }
        if (lastAddress2 ==null || address2!= lastAddress2) {
            lastAddress2 = address2;
            if (address2) {
                address2Pin.high();
            } else {
                address2Pin.low();
            }
        }
        if (lastAddress3 ==null || address3!= lastAddress3) {
            lastAddress3 = address3;
            if (address3) {
                address3Pin.high();
            } else {
                address3Pin.low();
            }
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
    public void red() {
        cycleColours = false;
        colour = RED;
        data1PinG.low();
        data2PinG.low();
    }

    @Override
    public void green() {
        cycleColours = false;
        colour = GREEN;
        data1PinR.low();
        data2PinR.low();
    }

    @Override
    public void yellow() {
        cycleColours = false;
        colour = YELLOW;
    }

    @Override
    public void blue() {
        System.err.println("What is 'blue'?");
    }

    @Override
    public void multi() {

    }

    @Override
    public void cycle(int ms) {
        cycleColours = true;
        colourChangePeriod = ms;
    }
}