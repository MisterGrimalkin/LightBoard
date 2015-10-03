package net.amarantha.lightboard.surface;

import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.utility.Sync;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LightBoardSurface {

    private int rows;
    private int cols;

    private double[][][] ledState;

    private final LightBoard board;
    private final Sync sync;

    protected Region boardRegion;

    @Inject
    public LightBoardSurface(LightBoard board, Sync sync) {
        this.board = board;
        this.sync = sync;

        rows = board.getRows();
        cols = board.getCols();

        ledState = new double[3][rows][cols];

        boardRegion = safeRegion(0, 0, cols, rows);

    }


    ///////////////////
    // Light Control //
    ///////////////////

    public LightBoardSurface init() {
        board.init();
        System.out.println("Starting LightBoardSurface....");
        sync.addTask(new Sync.Task(board.getUpdateInterval()) {
            @Override
            public void runTask() {
                board.update(ledState);
            }
        });
        System.out.println("Surface Active");
        return this;
    }

    public boolean isOn(int x, int y) {
        return pointInRegion(x, y, boardRegion)
                && (
                           ledState[0][y][x] >= 0.5
                        || ledState[1][y][x] >= 0.5
                        || ledState[2][y][x] >= 0.5
                )
        ;
    }

    public boolean drawPoint(int x, int y) {
        return drawPoint(x, y, 1.0, 1.0, 1.0, boardRegion);
    }

    public boolean clearPoint(int x, int y) {
        return drawPoint(x, y, 0.0, 0.0, 0.0, boardRegion);
    }

    public boolean drawPoint(int x, int y, Region r) {
        return drawPoint(x, y, 1.0, 1.0, 1.0, r);
    }

    public boolean clearPoint(int x, int y, Region r) {
        return drawPoint(x, y, 0.0, 0.0, 0.0, r);
    }

    public boolean drawPoint(int x, int y, double red, double green, double blue, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledState[0][y][x] = Math.max(Math.min(red, 1.0), 0.0);
            ledState[1][y][x] = Math.max(Math.min(green, 1.0), 0.0);
            ledState[2][y][x] = Math.max(Math.min(blue, 1.0), 0.0);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean drawPattern(int xPos, int yPos, Pattern pattern) {
        return drawPattern(xPos, yPos, pattern, boardRegion);
    }
    public synchronized boolean drawPattern(int xPos, int yPos, Pattern pattern, Region r) {
        return drawPattern(xPos, yPos, pattern, false, r);
    }

    public synchronized boolean drawPattern(int xPos, int yPos, Pattern pattern, boolean clearBackground, Region r) {
        boolean changed = false;
        double[][][] chr = pattern.getColourValues();
        if ( chr.length>=3 && chr[0].length>0 && chr[0][0].length> 0 ) {
            for (int x = 0; x < getCols(); x++) {
                for (int y = 0; y < getRows(); y++) {
                    if ( x-xPos >= 0 && x-xPos < chr[0][0].length && y-yPos >= 0 && y-yPos < chr[0].length ) {
                        int imgCol = x-xPos;
                        int imgRow = y-yPos;
                        changed |= drawPoint(x, y, chr[0][imgRow][imgCol], chr[1][imgRow][imgCol], chr[2][imgRow][imgCol], r);
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
        return clearRegion(boardRegion);
    }

    public synchronized boolean clearRegion(Region r) {
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            for ( int y=0; y<r.height; y++ ) {
                changed |= clearPoint(r.left+x, r.top+y);
            }
        }
        return changed;
    }

    public synchronized boolean fillRegion(Region r) {
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            for ( int y=0; y<r.height; y++ ) {
                changed |= drawPoint(r.left + x, r.top + y, r);
            }
        }
        return changed;
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
        boolean changed = false;
        for ( int x=0; x<r.width; x++ ) {
            if ( x==0 || x==r.width-1 ) {
                for ( int y=0; y<r.height; y++ ) {
                    changed |= drawPoint(r.left+x, r.top+y, r);
                }
            } else {
                changed |= drawPoint(r.left+x, r.top, r);
                changed |= drawPoint(r.left+x, r.bottom, r);
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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

}
