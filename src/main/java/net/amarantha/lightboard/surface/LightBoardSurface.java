package net.amarantha.lightboard.surface;

import net.amarantha.lightboard.board.CLightBoard;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.Colour;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.utility.Sync;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LightBoardSurface {

    private int rows;
    private int cols;

    private double[][][][] ledState;

    public static final int LAYERS = 10;

    private final LightBoard board;
    private final Sync sync;

    protected Region boardRegion;

    @Inject
    public LightBoardSurface(LightBoard board, Sync sync) {
        this.board = board;
        this.sync = sync;
    }

    ///////////////////
    // Light Control //
    ///////////////////

    public LightBoardSurface init(int rows, int cols) {
        return init(rows, cols, false);
    }

    public LightBoardSurface init(int rows, int cols, boolean loadTestPattern) {
        this.rows = rows;
        this.cols = cols;

        if (board instanceof CLightBoard) {
            new Thread(() -> board.init(rows, cols)).start();
        } else {
            board.init(rows, cols);
        }

        ledState = new double[LAYERS][3][rows][cols];

        if (loadTestPattern) {
            initTestPattern(0);
        }

        boardRegion = safeRegion(0, 0, cols, rows);

        System.out.println("Starting LightBoardSurface....");
        sync.addTask(new Sync.Task(board.getUpdateInterval()) {
            @Override
            public void runTask() {
                updateBoard();
            }
        });
        System.out.println("Surface Active");
        return this;
    }



    private void updateBoard() {
        double[][][] compositeState = new double[3][rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                compositeState[0][r][c] = ledState[0][0][r][c];
                compositeState[1][r][c] = ledState[0][1][r][c];
                compositeState[2][r][c] = ledState[0][2][r][c];
                for (int l = 1; l < LAYERS; l++) {
                    if (ledState[l][0][r][c] > 0) {
                        compositeState[0][r][c] = ledState[l][0][r][c];
                    }
                    if (ledState[l][1][r][c] > 0) {
                        compositeState[1][r][c] = ledState[l][1][r][c];
                    }
                    if (ledState[l][2][r][c] > 0) {
                        compositeState[2][r][c] = ledState[l][2][r][c];
                    }
                }
            }
        }

        board.update(compositeState);
    }

    private int testPatternSize = 6;
    private int testPatternOffset = 0;

    public void testMode() {
        System.out.println("LightBoard in TEST MODE");
        sync.addTask(new Sync.Task(100L) {
            @Override
            public void runTask() {
                clearSurface();
                if (testPatternOffset++ >= (testPatternSize * 3)) {
                    testPatternOffset = 1;
                }
                initTestPattern(testPatternOffset - (testPatternSize * 3));
            }
        });

    }

    private void initTestPattern(int xOffset) {
        for (int x = 0; x < (cols + (testPatternSize * 3)); x += (testPatternSize * 3)) {
            for (int y = 0; y < rows; y += (testPatternSize * 3)) {
                if (x + xOffset < cols) {
                    fillRegion(safeRegion(x + xOffset, y, testPatternSize, testPatternSize), new Colour(1.0, 0.0, 0.0));
                    if (x + xOffset + testPatternSize < cols && y + testPatternSize < rows) {
                        fillRegion(safeRegion(x + xOffset + testPatternSize, y + testPatternSize, testPatternSize, testPatternSize), new Colour(0.0, 1.0, 0.0));
                        if (x + xOffset + (testPatternSize * 2) < cols && y + (testPatternSize * 2) < rows) {
                            fillRegion(safeRegion(x + xOffset + (testPatternSize * 2), y + (testPatternSize * 2), testPatternSize, testPatternSize), new Colour(1.0, 1.0, 0.0));
                        }
                    }
                }
            }
        }
    }

    /////////////////
    // Point Query //
    /////////////////

    public boolean isOn(int x, int y) {
        return isOn(0, x, y);
    }

    public boolean isOn(int layer, int x, int y) {
        return pointInRegion(x, y, boardRegion)
                && (
                ledState[layer][0][y][x] >= 0.5
                        || ledState[layer][1][y][x] >= 0.5
                        || ledState[layer][2][y][x] >= 0.5
        )
                ;
    }

    ////////////////
    // Draw Point //
    ////////////////

    public boolean drawPoint(int layer, int x, int y, double red, double green, double blue, Region r) {
        if (pointInRegion(x, y, r)) {
            ledState[layer][0][y][x] = Math.max(Math.min(red, 1.0), 0.0);
            ledState[layer][1][y][x] = Math.max(Math.min(green, 1.0), 0.0);
            ledState[layer][2][y][x] = Math.max(Math.min(blue, 1.0), 0.0);
            return true;
        } else {
            return false;
        }
    }
    public boolean drawPoint(int layer, int x, int y, Colour colour, Region r) {
        return drawPoint(layer, x, y, colour.getRed(), colour.getGreen(), colour.getBlue(), r);
    }
    public boolean drawPoint(int layer, int x, int y, Colour colour) {
        return drawPoint(layer, x, y, colour.getRed(), colour.getGreen(), colour.getBlue(), boardRegion);
    }


    // Using Default Layer (0)
    public boolean drawPoint(int x, int y, double red, double green, double blue, Region r) {
        return drawPoint(0, x, y, red, green, blue, r);
    }
    public boolean drawPoint(int x, int y, Colour colour, Region r) {
        return drawPoint(0, x, y, colour, r);
    }
    public boolean drawPoint(int x, int y, Colour colour) {
        return drawPoint(0, x, y, colour, boardRegion);
    }

    // Boolean

    public boolean drawPoint(int layer, int x, int y, Region r) {
        return drawPoint(layer, x, y, 1.0, 1.0, 1.0, r);
    }
    public boolean drawPoint(int x, int y, Region r) {
        return drawPoint(0, x, y, 1.0, 1.0, 1.0, r);
    }
    public boolean drawPoint(int x, int y) {
        return drawPoint(0, x, y, 1.0, 1.0, 1.0, boardRegion);
    }


    /////////////////
    // Clear Point //
    /////////////////

    public boolean clearPoint(int x, int y) {
        return clearPoint(x, y, 0);
    }

    public boolean clearPoint(int layer, int x, int y) {
        return drawPoint(layer, x, y, 0.0, 0.0, 0.0, boardRegion);
    }

    public boolean clearPoint(int x, int y, Region r) {
        return drawPoint(x, y, 0.0, 0.0, 0.0, r);
    }

    /////////////
    // Pattern //
    /////////////

    public synchronized boolean drawPattern(int xPos, int yPos, Pattern pattern) {
        return drawPattern(xPos, yPos, pattern, boardRegion);
    }
    public synchronized boolean drawPattern(int xPos, int yPos, Pattern pattern, Region r) {
        return drawPattern(0, xPos, yPos, pattern, r);
    }
    public synchronized boolean drawPattern(int layer, int xPos, int yPos, Pattern pattern) {
        return drawPattern(layer, xPos, yPos, pattern, boardRegion);
    }

    public synchronized boolean drawPattern(int layer, int xPos, int yPos, Pattern pattern, Region r) {
        return drawPattern(layer, xPos, yPos, pattern, false, r);
    }
    public synchronized boolean drawPattern(int xPos, int yPos, Pattern pattern, boolean clearBackground, Region r) {
        return drawPattern(0, xPos, yPos, pattern, clearBackground, r);
    }

    public synchronized boolean drawPattern(int layer, int xPos, int yPos, Pattern pattern, boolean clearBackground, Region r) {
        boolean changed = false;
        double[][][] chr = pattern.getColourValues();
        if ( chr.length>=3 && chr[0].length>0 && chr[0][0].length> 0 ) {
            for (int x = 0; x < getCols(); x++) {
                for (int y = 0; y < getRows(); y++) {
                    if ( x-xPos >= 0 && x-xPos < chr[0][0].length && y-yPos >= 0 && y-yPos < chr[0].length ) {
                        int imgCol = x-xPos;
                        int imgRow = y-yPos;
                        double red = chr[0][imgRow][imgCol];
                        double green = chr[1][imgRow][imgCol];
                        double blue = chr[2][imgRow][imgCol];
                        if ( clearBackground || red>0.0 || green>0.0 || blue>0.0 ) {
                            changed |= drawPoint(layer, x, y, chr[0][imgRow][imgCol], chr[1][imgRow][imgCol], chr[2][imgRow][imgCol], r);
                        }
                    }
                }
            }
        }
        return changed;
    }

    public synchronized boolean clearPattern(int xPos, int yPos, boolean[][] chr) {
        return clearPattern(xPos, yPos, chr, boardRegion);
    }

    public synchronized boolean clearPattern(int xPos, int yPos, boolean[][] chr, Region r) {
        boolean changed = false;
        if ( chr.length>0 && chr[0].length> 0 ) {
            for (int x = 0; x < chr[0].length; x++) {
                for (int y = 0; y < chr.length; y++) {
                    if (chr[y][x]) {
                        changed |= clearPoint(x + xPos, y + yPos, r);
                    }
                }
            }
        }
        return changed;
    }

    public synchronized boolean clearSurface() {
        for ( int l=0; l<LAYERS; l++ ) {
            clearRegion(l, boardRegion);
        }
        return true;
    }

    public synchronized boolean clearRegion(Region r) {
        return clearRegion(0, r);
    }

    public synchronized boolean clearRegion(int layer, Region r) {
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            for ( int y=0; y<r.height; y++ ) {
                changed |= clearPoint(layer, r.left+x, r.top+y);
            }
        }
        return changed;
    }

    public synchronized boolean fillRegion(Region r) {
        return fillRegion(0, r, new Colour(1,1,1));
    }

    public synchronized boolean fillRegion(int layer, Region r, Colour colour) {
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            for ( int y=0; y<r.height; y++ ) {
                changed |= drawPoint(layer, r.left + x, r.top + y, colour, r);
            }
        }
        return changed;
    }

    public synchronized boolean fillRegion(Region r, Colour c) {
        return fillRegion(0, r, c);
    }

    public synchronized boolean invertRegion(Region r) {
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            for ( int y=0; y<r.height; y++ ) {
                int actualX = r.left+x;
                int actualY = r.top+y;
                if ( isOn(actualX, actualY) ) {
                    changed |= clearPoint(actualX, actualY, r);
                } else {
                    changed |= drawPoint(actualX, actualY, r);
                }
            }
        }
        return changed;
    }

    public synchronized boolean outlineRegion(Region r) {
        return outlineRegion(0, r);
    }

    public synchronized boolean outlineRegion(int layer, Region r) {
        return outlineRegion(layer, new Colour(1.0, 1.0, 1.0), r);
    }

    public synchronized boolean outlineRegion(int layer, Colour colour, Region r) {
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            if ( x==0 || x==r.width-1 ) {
                for ( int y=0; y<r.height; y++ ) {
                    changed |= drawPoint(layer, r.left+x, r.top+y, colour, r);
                }
            } else {
                changed |= drawPoint(layer, r.left+x, r.top, colour, r);
                changed |= drawPoint(layer, r.left+x, r.bottom, colour, r);
            }
        }
        return changed;
    }


    ////////////
    // Region //
    ////////////

    public Region safeRegion(int left, int top, int width, int height) {
        if ( left<0 ) {
            width += left;
        }
        if ( top<0 ) {
            height += top;
        }
        int safeLeft = left<0 ? 0 : left>=getCols() ? getCols()-1 : left;
        int safeTop = top<0 ? 0 : top>=getRows() ? getRows()-1 : top;
        int safeWidth = safeLeft+width > getCols() ? getCols()-safeLeft : width;
        int safeHeight = safeTop+height > getRows() ? getRows()-safeTop : height;
        return new Region(safeLeft, safeTop, safeWidth, safeHeight);
    }

    protected boolean pointInRegion(int x, int y, Region region) {
        if ( region==null ) {
            region = safeRegion(0, 0, getRows(), getCols());
        }
        return ( x>=region.left && x<=region.right && y>=region.top && y<=region.bottom );
    }

    public Region getBoardRegion() {
        return boardRegion;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void sleep() {
        board.sleep();
    }

    public void wake() {
        board.wake();
    }

}
