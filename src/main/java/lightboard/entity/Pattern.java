package lightboard.entity;

/**
 * A pattern of dots represented as both binary (boolean) and colour data
 */
public class Pattern {

    private boolean[][] binaryValues;
    private double[][][] colourValues;

    private Colour penColour = new Colour(1.0, 1.0, 1.0);

    public Pattern(int rows, int cols) {
        binaryValues = new boolean[rows][cols];
        colourValues = new double[3][rows][cols];
    }

    public void setPenColour(Colour penColour) {
        this.penColour = penColour;
    }

    public void drawPoint(int row, int col, boolean value) {
        if ( row < getRows() && col < getCols() ) {
            binaryValues[row][col] = value;
            colourValues[0][row][col] = (value ? penColour.getRed() : 0);
            colourValues[1][row][col] = (value ? penColour.getGreen() : 0);
            colourValues[2][row][col] = (value ? penColour.getBlue() : 0);
        }
    }

    public void drawPoint(int row, int col, Colour colour) {
        if ( row < getRows() && col < getCols() ) {
            binaryValues[row][col] = (colour.getRed()>=0.5 || colour.getGreen()>=0.5 || colour.getBlue()>=0.5);
            colourValues[0][row][col] = colour.getRed();
            colourValues[1][row][col] = colour.getGreen();
            colourValues[2][row][col] = colour.getBlue();
        }
    }

    public boolean getBinaryPoint(int row, int col) {
        return binaryValues[row][col];
    }

    public Colour getColourPoint(int row, int col) {
        return new Colour(colourValues[0][row][col], colourValues[1][row][col], colourValues[2][row][col]);
    }

    public int getRows() {
        return binaryValues.length;
    }

    public int getCols() {
        return binaryValues[0].length;
    }

    public boolean[][] getBinaryValues() {
        return binaryValues;
    }

    public double[][][] getColourValues() {
        return colourValues;
    }

}
