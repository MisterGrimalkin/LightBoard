package net.amarantha.lightboard.board.impl;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import net.amarantha.lightboard.board.ColourSwitcher;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.module.Cols;
import net.amarantha.lightboard.module.Rows;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static net.amarantha.lightboard.entity.Colour.*;

/**
 * The first real LED board implementation - the salvaged "Dan Light Board"
 */
public class RaspPiDanLightBoard implements LightBoard, ColourSwitcher {

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

    @Inject
    public RaspPiDanLightBoard(@Rows int rows, @Cols int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void init() {
        System.out.println("Starting Raspberry Pi LightBoard....");
        GpioController gpio = GpioFactory.getInstance();
        MCP23017GpioProvider gpioProvider = null;
        try {
            gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_0, 0x20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clockPin =  gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        storePin =  gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        outputPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        data1PinR = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
        data2PinR = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
        data1PinG = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
        data2PinG = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
        address0Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A0, PinState.LOW);
        address1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A1, PinState.LOW);
        address2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A2, PinState.LOW);
        address3Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A3, PinState.LOW);
        outputEnable1Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A4, PinState.HIGH);
        outputEnable2Pin = gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_A5, PinState.HIGH);
        data1PinR.low();
        data2PinR.low();
        System.out.println("Board Ready");
    }

    private static final int RED_MODE = 1;
    private static final int GREEN_MODE = 2;
    private static final int YELLOW_MODE = 3;

    private boolean cycleColours = false;
    private int colour = RED_MODE;

    private long t = 0;
    private long colourChangePeriod = 30000;

    @Override
    public void update(double[][][] data) {
        boolean[][] boolData = new boolean[rows][cols];
        for ( int r=0; r<rows; r++ ) {
            for ( int c=0; c<cols; c++ ) {
                boolData[r][c] = data[0][r][c] >= 0.5 || data[1][r][c] >= 0.5 || data[2][r][c] >= 0.5;
            }
        }
        update(boolData);
    }

    public void update(boolean[][] data) {
        if ( cycleColours && System.currentTimeMillis()-t > colourChangePeriod) {
            colour++;
            if ( colour> YELLOW_MODE) colour = RED_MODE;
            t = System.currentTimeMillis();
        }
        try {
            for (int row = 0; row < data.length; row++) {
                sendSerialString(data[row]);
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

    private void sendSerialString(boolean[] data) throws InterruptedException {
        Boolean lastValue1 = null;
        Boolean lastValue2 = null;
        for (int col = (data.length/2)-1; col >= 0; col--) {
            int parallelCol = col + (cols /2);
            clockPin.low();
            if ( lastValue1==null || lastValue1!=data[col] ) {
                lastValue1 = data[col];
                if (data[col]) {
                    if ( colour!= GREEN_MODE) data1PinR.high();
                    if ( colour!= RED_MODE) data1PinG.high();
                } else {
                    if ( colour!= GREEN_MODE) data1PinR.low();
                    if ( colour!= RED_MODE) data1PinG.low();
                }
            }
            if ( lastValue2==null || lastValue2!=data[parallelCol] ) {
                lastValue2 = data[parallelCol];
                if (data[parallelCol]) {
                    if ( colour!= GREEN_MODE) data2PinR.high();
                    if ( colour!= RED_MODE) data2PinG.high();
                } else {
                    if ( colour!= GREEN_MODE) data2PinR.low();
                    if ( colour!= RED_MODE) data2PinG.low();
                }
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

    }

    @Override
    public void wake() {

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
        if (RED.equals(colourName)) {
            cycleColours = false;
            colour = RED_MODE;
            data1PinG.low();
            data2PinG.low();
        } else if (GREEN.equals(colourName)) {
            cycleColours = false;
            colour = GREEN_MODE;
            data1PinR.low();
            data2PinR.low();
        } else if (YELLOW.equals(colourName)) {
            cycleColours = false;
            colour = YELLOW_MODE;
        }
    }

}
