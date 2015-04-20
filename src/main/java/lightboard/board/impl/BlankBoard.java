package lightboard.board.impl;

import com.pi4j.io.gpio.*;
import lightboard.board.LightBoard;

import java.util.Timer;
import java.util.TimerTask;

public class BlankBoard implements LightBoard {

    final GpioController gpio = GpioFactory.getInstance();

    GpioPinDigitalOutput clockPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
    GpioPinDigitalOutput storePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    GpioPinDigitalOutput add0Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
    GpioPinDigitalOutput add1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.HIGH);
    GpioPinDigitalOutput add2Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    GpioPinDigitalOutput add3Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
    GpioPinDigitalOutput data1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
    GpioPinDigitalOutput outputEnable1Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.HIGH);

    private int rows;
    private int cols;

    public BlankBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    private int addr = 0;

    @Override
    public void init() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(addr);

                if ( (addr&8)==8 ) {
                    add3Pin.high();
                } else {
                    add3Pin.low();
                }

                if ( (addr&4)==4 ) {
                    add2Pin.high();
                } else {
                    add2Pin.low();
                }

                if ( (addr&2)==2 ) {
                    add1Pin.high();
                } else {
                    add1Pin.low();
                }

                if ( (addr&1)==1 ) {
                    add0Pin.high();
                } else {
                    add0Pin.low();
                }

                if ( addr++ >= 8 ) {
                    addr = 0;
                }
            }
        }, 0, 2000);
    }

    @Override
    public void dump(boolean[][] data) {

        try {
            for ( int c=0; c<data[0].length; c++ ) {

                push(true);
                push(false);
                push(false);
                push(false);
                push(true);
                push(true);
                push(false);
                push(true);
                push(false);
                push(false);
            }

            storePin.low();
            Thread.sleep(0, 500);
            storePin.high();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

    }

    private void push(boolean b) {
        try {
            clockPin.high();
            if ( b ) {
                data1Pin.high();
            } else {
                data1Pin.low();
            }
            Thread.sleep(0,500);
            clockPin.low();
            Thread.sleep(0,500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRefreshInterval() {
        return 1000;
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
