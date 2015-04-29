package lightboard.board.impl;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import lightboard.board.LightBoard;

import java.io.IOException;
import java.math.BigInteger;

public class RaspberryPiLightBoard implements LightBoard {

    public static RaspberryPiLightBoard makeBoard1() {
        try {
            GpioController gpio = GpioFactory.getInstance();
            MCP23017GpioProvider gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_0, 0x20);
            return new RaspberryPiLightBoard(16, 90, 1,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A1, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A2, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A3, PinState.HIGH),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A4, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A5, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A6, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.HIGH),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B0, PinState.HIGH),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B1, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B2, PinState.HIGH)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RaspberryPiLightBoard makeBoard2() {
        try {
            GpioController gpio = GpioFactory.getInstance();
            MCP23017GpioProvider gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_0, 0x21);
            return new RaspberryPiLightBoard(16, 90, 1,
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A1, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A2, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A3, PinState.HIGH),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A4, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A5, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A6, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.HIGH),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B0, PinState.HIGH),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B1, PinState.LOW),
                gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B2, PinState.HIGH)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int rows;
    private int cols;
    private int refreshInterval;

    private GpioPinDigitalOutput clockPin;
    private GpioPinDigitalOutput storePin;
    private GpioPinDigitalOutput outputEnableRowsPin;
    private GpioPinDigitalOutput address0Pin;
    private GpioPinDigitalOutput address1Pin;
    private GpioPinDigitalOutput address2Pin;
    private GpioPinDigitalOutput address3Pin;
    private GpioPinDigitalOutput data1Pin;
    private GpioPinDigitalOutput outputEnable1Pin;
    private GpioPinDigitalOutput data2Pin;
    private GpioPinDigitalOutput outputEnable2Pin;

    public RaspberryPiLightBoard(int rows, int cols, int refreshInterval, GpioPinDigitalOutput clockPin, GpioPinDigitalOutput storePin, GpioPinDigitalOutput outputEnableRowsPin, GpioPinDigitalOutput address0Pin, GpioPinDigitalOutput address1Pin, GpioPinDigitalOutput address2Pin, GpioPinDigitalOutput address3Pin, GpioPinDigitalOutput data1Pin, GpioPinDigitalOutput outputEnable1Pin, GpioPinDigitalOutput data2Pin, GpioPinDigitalOutput outputEnable2Pin) {
        this.rows = rows;
        this.cols = cols;
        this.refreshInterval = refreshInterval;
        this.clockPin = clockPin;
        this.storePin = storePin;
        this.outputEnableRowsPin = outputEnableRowsPin;
        this.address0Pin = address0Pin;
        this.address1Pin = address1Pin;
        this.address2Pin = address2Pin;
        this.address3Pin = address3Pin;
        this.data1Pin = data1Pin;
        this.outputEnable1Pin = outputEnable1Pin;
        this.data2Pin = data2Pin;
        this.outputEnable2Pin = outputEnable2Pin;
    }

    private boolean wait = false;

    @Override
    public void init() {

    }

    @Override
    public void dump(boolean[][] data) {
        wait = true;
        try {
            for (int row = 0; row < data.length; row++) {
//                long t = System.currentTimeMillis();
                sendSerialString(data[row]);
                outputEnableRowsPin.high();
                decodeRowAddress(row);
//                outputEnable1Pin.low();
                storePin.high();
                storePin.low();
                outputEnableRowsPin.low();
//                outputEnable1Pin.high();
//        System.out.println(System.currentTimeMillis()-t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wait = false;
        }
    }



    private void sendSerialString(boolean[] data) throws InterruptedException {
        Boolean lastValue = null;
        for (int col = data.length - 1; col >= 0; col--) {
            clockPin.low();
            if ( lastValue==null || lastValue!=data[col] ) {
                lastValue = data[col];
                if (data[col]) {
                    data1Pin.high();
//                    data2Pin.high();
                } else {
                    data1Pin.low();
//                    data2Pin.low();
                }
            }
            clockPin.high();
        }
    }

    private Boolean lastAddress0 = null;
    private Boolean lastAddress1 = null;
    private Boolean lastAddress2 = null;
    private Boolean lastAddress3 = null;

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

    @Override
    public int getRefreshInterval() {
        return refreshInterval;
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
    public boolean saysWait() {
        return wait;
    }

}
