package lightboard.board.surface;

import lightboard.board.PolychromeLightBoard;

import java.util.Timer;
import java.util.TimerTask;

public class PolychromeLightBoardSurface extends MonochromeLightBoardSurface {

    private final double[][][] ledPolyValue;

    private final PolychromeLightBoard[] boards;

    public PolychromeLightBoardSurface(PolychromeLightBoard... boards) {
        super(boards);
        this.boards = boards;
        ledPolyValue = new double[getRows()][getCols()][3];
    }


    ///////////////////
    // Light Control //
    ///////////////////

    @Override
    public LightBoardSurface init() {
        for ( int i=0; i<boards.length; i++ ) {
            final PolychromeLightBoard board = boards[i];
            new Timer(true).schedule(
                    new TimerTask() { @Override public void run() {
                        board.dump(ledPolyValue);
                    }},
                    1000, board.getRefreshInterval()
            );
        }
        return this;
    }

    @Override
    public boolean isOn(int x, int y) {
        return pointInRegion(x, y, boardRegion) && (ledPolyValue[y][x][0]>0 || ledPolyValue[y][x][1]>0 || ledPolyValue[y][x][2]>0);
    }

    @Override
    public boolean drawPoint(int x, int y, double value, Region r) {
        return drawPoint(x, y, value, value, value, r);
    }

    public boolean drawPoint(int x, int y, double red, double green, double blue, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledPolyValue[y][x][0] = Math.max(Math.min(red, 1.0), 0.0);
            ledPolyValue[y][x][1] = Math.max(Math.min(green, 1.0), 0.0);
            ledPolyValue[y][x][2] = Math.max(Math.min(blue, 1.0), 0.0);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean drawPattern(int xPos, int yPos, double[][][] chr, boolean clearBackground, Region r) {
        boolean changed = false;
        if ( chr.length>0 && chr[0].length> 0 ) {
            for (int x = 0; x < getCols(); x++) {
                for (int y = 0; y < getRows(); y++) {
                    if ( x-xPos >= 0 && x-xPos < chr[0].length && y-yPos >= 0 && y-yPos < chr.length ) {
                        double[] pixel = chr[y - yPos][x - xPos];
                        changed |= drawPoint(x, y, pixel[0], pixel[1], pixel[2], r);
                    }
                }
            }
        }
        return changed;
    }

}
