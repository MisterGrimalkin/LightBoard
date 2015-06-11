package lightboard.entity;

/**
 * Simple RGB colour
 */
public class Colour {

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
