package lightboard.font;

import lightboard.util.MessageQueue.HPosition;

import java.util.HashMap;
import java.util.Map;

public abstract class Font {

    public final static char NL = '\n';

    private Map<Character, boolean[][]> chars = new HashMap<>();

    public final void registerPattern(char key, boolean[][] bits) {
        chars.put(key, bits);
    }

    public final boolean[][] getPattern(char key) {
        return chars.get(key);
    }

    public final int getHeight(char key) {
        boolean[][] c = getPattern(key);
        if ( c!=null ) {
            return c.length;
        }
        return 0;
    }

    public final int getWidth(char key) {
        boolean[][] c = getPattern(key);
        if ( c!=null ) {
            return c[0].length;
        }
        return 0;
    }

    public final int getStringWidth(String str) {
        int width = 0;
        int rowWidth = 0;
        boolean inTag = false;
        for ( int i=0; i<str.length(); i++ ) {
            char c = str.charAt(i);
            if ( inTag ) {
                if ( c=='}' ) {
                    inTag = false;
                }
            } else {
                if ( c=='{' ) {
                    inTag = true;
                } else {
                    if (i == str.length() - 1) {
                        rowWidth += getWidth(c);
                        width = Math.max(rowWidth, width);
                    } else if (c == NL) {
                        width = Math.max(rowWidth, width);
                        rowWidth = 0;
                    } else {
                        rowWidth += getWidth(c) + 1;
                    }
                }
            }
        }
        return width;
    }

    public final int getStringHeight(String str) {
        int height = 0;
        int rowHeight = 0;
        boolean inTag = false;
        for ( int i=0; i<str.length(); i++ ) {
            char c = str.charAt(i);
            if ( inTag ) {
                if ( c=='}' ) {
                    inTag = false;
                }
            } else {
                if ( c=='{' ) {
                    inTag = true;
                } else {
                    rowHeight = Math.max(rowHeight, getHeight(c));
                    if (i == str.length() - 1) {
                        height += rowHeight;
                    } else if (c == NL) {
                        height += rowHeight;
                        rowHeight = 0;
                    }
                }
            }
        }
        return height;
    }

    public final Pattern renderString(String str) {
        return renderString(str, HPosition.LEFT);
    }

    public final Pattern renderString(String str, HPosition align) {
        if ( str==null || str.isEmpty() ) {
            return new Pattern(1,1);
        }
        int cols = getStringWidth(str);
        int rows = getStringHeight(str);
        Pattern result = new Pattern(rows, cols);
        String[] lines = str.split("\n");
        if ( lines.length==1 ) {
            int cursorX = 0;
            boolean inTag = false;
            String tag = "";
            for ( int c=0; c<str.length(); c++ ) {
                char chr = str.charAt(c);
                if ( inTag ) {
                    if ( chr=='}' ) {
                        if ( "red".equals(tag) ) {
                            result.setPenColour(new Colour(1.0,0.0,0.0));
                        } else if ( "green".equals(tag) ) {
                            result.setPenColour(new Colour(0.0,1.0,0.0));
                        } else if ( "yellow".equals(tag) ) {
                            result.setPenColour(new Colour(1.0,1.0,0.0));
                        }
                        tag = "";
                        inTag = false;
                    } else {
                        tag += chr;
                    }
                } else {
                    if ( chr=='{' ) {
                        inTag = true;
                    } else {
                        boolean[][] pattern = getPattern(chr);
                        if (pattern != null) {
                            for (int row = 0; row < pattern.length; row++) {
                                for (int col = 0; col < pattern[0].length; col++) {
                                    result.setPoint(row, col+cursorX, pattern[row][col]);
                                }
                            }
                            cursorX += getWidth(chr) + 1;
                        }
                    }
                }
            }
        } else {
            int cursorY = 0;
            for (int l = 0; l < lines.length; l++) {
                String line = lines[l];
                int cursorX = 0;
                if ( align==HPosition.RIGHT ) {
                    cursorX = cols - getStringWidth(line);
                } else if ( align==HPosition.CENTRE ) {
                    cursorX = ( cols - getStringWidth(line) ) / 2;
                }
                int lineHeight = getStringHeight(line);
                Pattern pattern = renderString(line);
                for ( int row=0; row<pattern.rows(); row++ ) {
                    for ( int col=0; col<pattern.cols(); col++ ) {
                        result.setPoint(row+cursorY, col+cursorX, pattern.getBinaryPoint(row,col));
                    }
                }
                cursorY += lineHeight;
            }
        }
        return result;
    }

    public class Pattern {
        private boolean[][] binaryValues;
        private double[][][] colourValues;
        private Colour penColour = new Colour(1.0, 1.0, 1.0);
        public Pattern(int rows, int cols) {
            binaryValues = new boolean[rows][cols];
            colourValues = new double[3][rows][cols];
        }
        public void setPoint(int row, int col, boolean value) {
            if ( row < rows() && col < cols() ) {
                binaryValues[row][col] = value;
                colourValues[0][row][col] = (value ? penColour.red : 0);
                colourValues[1][row][col] = (value ? penColour.green : 0);
                colourValues[2][row][col] = (value ? penColour.blue : 0);
            }
        }
        public boolean getBinaryPoint(int row, int col) {
            return binaryValues[row][col];
        }
        public Colour getColourPoint(int row, int col) {
            return new Colour(colourValues[0][row][col], colourValues[1][row][col], colourValues[2][row][col]);
        }
        public int rows() {
            return binaryValues.length;
        }
        public int cols() {
            return binaryValues[0].length;
        }
        public boolean[][] getBinaryValues() {
            return binaryValues;
        }
        public double[][][] getColourValues() {
            return colourValues;
        }
        public void setPenColour(Colour penColour) {
            this.penColour = penColour;
        }
    }

    public class Colour {
        private double red;
        private double green;
        private double blue;
        public Colour(double red, double green, double blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

}
