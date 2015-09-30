package net.amarantha.lightboard.entity;

/**
 * Simple RGB colour
 */
public class Colour {

    // Colour names used for webservice
    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String BLUE = "blue";
    public static final String YELLOW = "yellow";
    public static final String MULTI = "multi";

    private double red;
    private double green;
    private double blue;

    public Colour(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

}
