package net.amarantha.lightboard.surface;

import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.util.Sync;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The surface holds the state and writes to the board
 */
public class LightBoardSurface {

    private int rows;
    private int cols;

    protected Region boardRegion;

    private boolean[][] ledStatus;

    private final LightBoard[] boards;

    public LightBoardSurface(LightBoard... boards) {

        this.boards = boards;

        if ( boards.length>0 ) {

            LightBoard firstBoard = boards[0];
            rows = firstBoard.getRows();
            cols = firstBoard.getCols();
            ledStatus = new boolean[rows][cols];

            boardRegion = safeRegion(0, 0, cols, rows);

            for (int i = 0; i < boards.length; i++) {
                LightBoard board = boards[i];
                if (board.getRows() != rows || board.getCols() != cols) {
                    throw new IllegalStateException("All LightBoards must have the same dimensions");
                }
            }

        }
    }

    public LightBoardSurface init() {
        System.out.println("Starting LightBoard Surface....");
        for ( final LightBoard board : boards ) {
            Sync.addTask(new Sync.Task(board.getRefreshInterval()) {
                @Override
                public void runTask() {
                    board.dump(ledStatus);
                }
            });
        }
        System.out.println("Surface Active");
        return this;
    }


    ///////////////////
    // Light Control //
    ///////////////////

    public boolean isOn(int x, int y) {
        return pointInRegion(x, y, boardRegion) && ledStatus[y][x];
    }

    public boolean drawPoint(int x, int y) {
        return drawPoint(x, y, boardRegion);
    }

    public boolean drawPoint(int x, int y, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledStatus[y][x] = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean clearPoint(int x, int y) {
        return clearPoint(x, y, boardRegion);
    }

    public boolean clearPoint(int x, int y, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledStatus[y][x] = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean drawPattern(int xPos, int yPos, boolean[][] chr) {
        return drawPattern(xPos, yPos, chr, boardRegion);
    }
    public synchronized boolean drawPattern(int xPos, int yPos, boolean[][] chr, Region r) {
        return drawPattern(xPos, yPos, chr, false, r);
    }

    public synchronized boolean drawPattern(int xPos, int yPos, boolean[][] chr, boolean clearBackground, Region r) {
        boolean changed = false;
        if ( chr.length>0 && chr[0].length> 0 ) {
            for (int x = 0; x < chr[0].length; x++) {
                for (int y = 0; y < chr.length; y++) {
                    if (chr[y][x]) {
                        changed |= drawPoint(x + xPos, y + yPos, r);
                    } else if ( clearBackground ) {
                        changed |= clearPoint(x + xPos, y + yPos, r);
                    }
                }
            }
        }
        return changed;
    }

    public boolean clearPattern(int xPos, int yPos, boolean[][] chr) {
        return clearPattern(xPos, yPos, chr, boardRegion);
    }

    public boolean clearPattern(int xPos, int yPos, boolean[][] chr, Region r) {
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

    public boolean clearSurface() {
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


    ///////////////
    // Self Test //
    ///////////////

    double testX = 0;
    double testY = 0;
    int testDeltaX = 1;
    int testDeltaY = 1;
    int testBounces = 0;
    boolean testInvert = false;

    private boolean[][] testPattern =
                    {{true,false,true},
                    {false,true,false},
                    {true,false,true}};

    public void selfTest() {
        selfTest(20, 0.5);
    }

    public void selfTest(int tick, final double speed) {

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {

                clearSurface();

                int floorX = (int) Math.floor(testX);
                int floorY = (int) Math.floor(testY);

                fillRegion(safeRegion(0, floorY, getCols(), 1));
                fillRegion(safeRegion(floorX, 0, 1, getRows()));

                outlineRegion(safeRegion(floorX + 2, floorY + 2, 3, 3));
                fillRegion(safeRegion(floorX - 4, floorY - 4, 3, 3));
                clearPoint(floorX - 3, floorY - 3);

                drawPattern(floorX - 4, floorY + 2, testPattern);
                drawPattern(floorX + 2, floorY - 4, testPattern);

                drawPoint(floorX - 4, floorY - 5);
                drawPoint(floorX - 4, floorY + 5);
                drawPoint(floorX + 4, floorY - 5);
                drawPoint(floorX + 4, floorY + 5);
                drawPoint(floorX - 5, floorY - 4);
                drawPoint(floorX - 5, floorY + 4);
                drawPoint(floorX + 5, floorY - 4);
                drawPoint(floorX + 5, floorY + 4);

                if (testInvert) {
                    invertRegion(boardRegion);
                }

                if (floorX < 0 || floorX >= getCols()) {
                    testX = floorX - testDeltaX;
                    testDeltaX = -testDeltaX;
                    testBounces++;
                }

                if (floorY < 0 || floorY >= getRows()) {
                    testY = floorY - testDeltaY;
                    testDeltaY = -testDeltaY;
                    testBounces++;
                }

                if (testBounces > 9) {
                    testInvert = !testInvert;
                    testBounces = 0;
                }

                testX = testX + (testDeltaX * speed);
                testY = testY + (testDeltaY * speed);

            }
        },100,tick);

    }

}
