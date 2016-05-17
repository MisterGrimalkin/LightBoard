package net.amarantha.lightboard.entity;

/**
 * Simple RGB colour
 */
public class Colour {

    // Colour names used for webservice
    public static final String RED_STR = "red";
    public static final String GREEN_STR = "green";
    public static final String BLUE_STR = "blue";
    public static final String YELLOW_STR = "yellow";
    public static final String MULTI_STR = "multi";

    // Standard Colours
    public static final Colour RED =    new Colour(1, 0, 0);
    public static final Colour GREEN =  new Colour(0, 1, 0);
    public static final Colour YELLOW = new Colour(1, 1, 0);

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
