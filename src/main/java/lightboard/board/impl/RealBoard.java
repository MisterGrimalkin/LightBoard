package lightboard.board.impl;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.impl.I2CDeviceImpl;
import com.pi4j.wiringpi.Gpio;
import lightboard.board.LightBoard;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

public class RealBoard implements LightBoard {

    final GpioController gpio = GpioFactory.getInstance();

    // create custom MCP23017 GPIO provider
    MCP23017GpioProvider gpioProvider;

//    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
//    GpioPinDigitalOutput storePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
//    GpioPinDigitalOutput add0Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
//    GpioPinDigitalOutput add1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
//    GpioPinDigitalOutput add2Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
//    GpioPinDigitalOutput add3Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
//    GpioPinDigitalOutput data1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
//    GpioPinDigitalOutput outputEnable1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);

    GpioPinDigitalOutput clockPin;
    GpioPinDigitalOutput storePin;
    GpioPinDigitalOutput outputEnableRowsPin;
    GpioPinDigitalOutput addr0Pin;
    GpioPinDigitalOutput addr1Pin;
    GpioPinDigitalOutput addr2Pin;
    GpioPinDigitalOutput addr3Pin;
    GpioPinDigitalOutput data1Pin;
    GpioPinDigitalOutput outputEnable1Pin;
    GpioPinDigitalOutput data2Pin;
    GpioPinDigitalOutput outputEnable2Pin;
//    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B3, "MyOutput-B3", PinState.LOW);
//    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B4, "MyOutput-B4", PinState.LOW);
//    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B5, "MyOutput-B5", PinState.LOW);
//    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B6, "MyOutput-B6", PinState.LOW);
//    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B7, "MyOutput-B7", PinState.LOW);


    private int rows;
    private int cols;
    private int address;
    private int value;

    public RealBoard(int rows, int cols, int address, int value) {
        this.rows = rows;
        this.cols = cols;
        this.address = address;
        this.value = value;
    }


    @Override
    public void init() {
        try {
            gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_0, 0x20);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clockPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A0, "MyOutput-B0", PinState.LOW);
        storePin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A1, "MyOutput-B1", PinState.LOW);
        outputEnableRowsPin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A2, "MyOutput-B2", PinState.LOW);
        addr0Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A3, "MyOutput-B3", PinState.HIGH);
        addr1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A4, "MyOutput-B4", PinState.LOW);
        addr2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A5, "MyOutput-B5", PinState.LOW);
        addr3Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A6, "MyOutput-B6", PinState.LOW);
        data1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A7, "MyOutput-B7", PinState.LOW);
        outputEnable1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B0, "MyOutput-B0", PinState.HIGH);
        data2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B1, "MyOutput-B1", PinState.LOW);
        outputEnable2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B2, "MyOutput-B2", PinState.HIGH);

    }

    @Override
    public void dump(boolean[][] data) {

        long t = System.currentTimeMillis();
        for (int row = 0; row < data.length; row++) {
            if (BigInteger.valueOf(row).testBit(0)) {
                addr0Pin.high();
            } else {
                addr0Pin.low();
            }
            if (BigInteger.valueOf(row).testBit(1)) {
                addr1Pin.high();
            } else {
                addr1Pin.low();
            }
            if (BigInteger.valueOf(row).testBit(2)) {
                addr2Pin.high();
            } else {
                addr2Pin.low();
            }
            if (BigInteger.valueOf(row).testBit(3)) {
                addr3Pin.high();
            } else {
                addr3Pin.low();
            }

            sendSerialString(data[row]);
        }

        System.out.println(System.currentTimeMillis()-t);
    }

    private void sendSerialString(boolean[] data) {
        for (int col = 0; col < data.length; col++) {
            clockPin.low();
            if (data[col]) {
                data1Pin.high();
            } else {
                data1Pin.low();
            }
            clockPin.high();
        }
        storePin.high();
        storePin.low();
    }


    @Override
    public int getRefreshInterval() {
        return 450;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }
}
