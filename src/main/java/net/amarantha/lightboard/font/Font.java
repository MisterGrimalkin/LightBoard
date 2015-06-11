package net.amarantha.lightboard.font;

import net.amarantha.lightboard.entity.Colour;
import net.amarantha.lightboard.entity.HPosition;
import net.amarantha.lightboard.entity.Pattern;

import java.util.HashMap;
import java.util.Map;

import static net.amarantha.lightboard.entity.HPosition.*;

public abstract class Font {

    public final static char NL = '\n';

    private Map<Character, Pattern> chars = new HashMap<>();

    public final void registerPattern(char key, boolean[][] bits) {
        registerPattern(key, new Pattern(bits));
    }

    public final void registerPattern(char key, Pattern pattern) {
        chars.put(key, pattern);
    }

    public final Pattern getPattern(char key) {
        return chars.get(key);
    }

    public final int getHeight(char key) {
        Pattern c = getPattern(key);
        if ( c!=null ) {
            return c.getRows();
        }
        return 0;
    }

    public final int getWidth(char key) {
        Pattern c = getPattern(key);
        if ( c!=null ) {
            return c.getCols();
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
        return renderString(str, LEFT);
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
            boolean penMode = false;
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
                        penMode = true;
                    } else {
                        Pattern pattern = getPattern(chr);
                        if (pattern != null) {
                            for (int row = 0; row < pattern.getRows(); row++) {
                                for (int col = 0; col < pattern.getCols(); col++) {
                                    if ( penMode ) {
                                        result.drawPoint(row, col + cursorX, pattern.getBinaryPoint(row, col));
                                    } else {
                                        result.drawPoint(row, col + cursorX, pattern.getColourPoint(row, col));
                                    }
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
                if ( align== RIGHT ) {
                    cursorX = cols - getStringWidth(line);
                } else if ( align==CENTRE ) {
                    cursorX = ( cols - getStringWidth(line) ) / 2;
                }
                int lineHeight = getStringHeight(line);
                Pattern pattern = renderString(line);
                for ( int row=0; row<pattern.getRows(); row++ ) {
                    for ( int col=0; col<pattern.getCols(); col++ ) {
                        result.drawPoint(row + cursorY, col + cursorX, pattern.getColourPoint(row, col));
                    }
                }
                cursorY += lineHeight;
            }
        }
        return result;
    }

}
