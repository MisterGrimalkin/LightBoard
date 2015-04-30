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
        for ( int i=0; i<str.length(); i++ ) {
            char c = str.charAt(i);
            if ( i==str.length()-1 ) {
                rowWidth += getWidth(c);
                width = Math.max(rowWidth, width);
            } else if ( c==NL ) {
                width = Math.max(rowWidth, width);
                rowWidth = 0;
            } else {
                rowWidth += getWidth(c)+1;
            }
        }
        return width;
    }

    public final int getStringHeight(String str) {
        int height = 0;
        int rowHeight = 0;
        for ( int i=0; i<str.length(); i++ ) {
            char c = str.charAt(i);
            rowHeight = Math.max(rowHeight, getHeight(c));
            if ( i==str.length()-1 ) {
                height += rowHeight;
            } else if ( c==NL ) {
                height += rowHeight;
                rowHeight = 0;
            }
        }
        return height;
    }

    public final boolean[][] renderString(String str) {
        return renderString(str, HPosition.LEFT);
    }

    public final boolean[][] renderString(String str, HPosition align) {
        int cols = getStringWidth(str);
        int rows = getStringHeight(str);
        boolean[][] result = new boolean[rows][cols];
        String[] lines = str.split("\n");
        if ( lines.length==1 ) {
            int cursorX = 0;
            for ( int c=0; c<str.length(); c++ ) {
                char chr = str.charAt(c);
                boolean[][] pattern = getPattern(chr);
                if ( pattern!=null ) {
                    for (int row = 0; row < pattern.length; row++) {
                        for (int col = 0; col < pattern[0].length; col++) {
                            result[row][col + cursorX] = pattern[row][col];
                        }
                    }
                    cursorX += getWidth(chr) + 1;
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
                boolean[][] pattern = renderString(line);
                for ( int row=0; row<pattern.length; row++ ) {
                    for ( int col=0; col<pattern[0].length; col++ ) {
                        result[row+cursorY][col+cursorX] = pattern[row][col];
                    }
                }
                cursorY += lineHeight;
            }
        }
        return result;
    }

}
