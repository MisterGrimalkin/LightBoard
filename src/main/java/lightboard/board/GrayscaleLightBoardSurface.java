package lightboard.board;

import lightboard.board.LightBoardSurface.Region;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by grimalkin on 08/04/15.
 */
public class GrayscaleLightBoardSurface {

    private final int rows;
    private final int cols;

    private final Region boardRegion;

    private final double[][] ledStatus;

    private final GrayscaleLightBoard[] boards;

    public GrayscaleLightBoardSurface(GrayscaleLightBoard... boards) {

        this.boards = boards;

        if ( boards.length==0 ) {
            throw new IllegalStateException("You must specify at least one LightBoard");
        }

        GrayscaleLightBoard firstBoard = boards[0];
        rows = firstBoard.getRows();
        cols = firstBoard.getCols();
        ledStatus = new double[rows][cols];

        boardRegion = safeRegion(0,0,cols,rows);

        for ( int i=0; i<boards.length; i++ ) {
            GrayscaleLightBoard board = boards[i];
            if (board.getRows() != rows || board.getCols() != cols) {
                throw new IllegalStateException("All LightBoards must have the same dimensions");
            }
        }

    }

    public GrayscaleLightBoardSurface init() {
        for ( int i=0; i<boards.length; i++ ) {
            final GrayscaleLightBoard board = boards[i];
            new Timer(true).schedule(
                    new TimerTask() { @Override public void run() {
                        board.dump(ledStatus);
                    }},
                    1000, board.getRefreshInterval()
            );
        }
        return this;
    }


    ///////////////////
    // Light Control //
    ///////////////////

    public boolean drawPoint(int x, int y, double value) {
        return drawPoint(x, y, value, boardRegion);
    }

    public boolean drawPoint(int x, int y, double value, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledStatus[y][x] = value;
            return true;
        } else {
            return false;
        }
    }

    public boolean drawPattern(int xPos, int yPos, double[][] chr) {
        return drawPattern(xPos, yPos, chr, boardRegion);
    }
    public synchronized boolean drawPattern(int xPos, int yPos, double[][] chr, Region r) {
        return drawPattern(xPos, yPos, chr, false, r);
    }

    public synchronized boolean drawPattern(int xPos, int yPos, double[][] chr, boolean clearBackground, Region r) {
        boolean changed = false;
        if ( chr.length>0 && chr[0].length> 0 ) {
            for (int x = 0; x < chr[0].length; x++) {
                for (int y = 0; y < chr.length; y++) {
                    changed |= drawPoint(x + xPos, y + yPos, chr[y][x], r);
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
                changed |= drawPoint(r.left + x, r.top + y, 0);
            }
        }
        return changed;
    }



    public Region safeRegion(int left, int top, int width, int height) {
        if ( left<0 ) {
            width += left;
        }
        if ( top<0 ) {
            height += top;
        }
        int safeLeft = left<0 ? 0 : left>=cols ? cols-1 : left;
        int safeTop = top<0 ? 0 : top>=rows ? rows-1 : top;
        int safeWidth = safeLeft+width > cols ? cols-safeLeft : width;
        int safeHeight = safeTop+height > rows ? rows-safeTop : height;
        return new Region(safeLeft, safeTop, safeWidth, safeHeight);
    }

    private boolean pointInRegion(int x, int y, Region region) {
        if ( region==null ) {
            region = new Region(0,0,rows,cols);
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
