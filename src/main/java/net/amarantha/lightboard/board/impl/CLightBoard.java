package net.amarantha.lightboard.board.impl;

public class CLightBoard {

    native void init();

    native void dump(double[][][] data);

    static {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("lightboard");
    }

}
