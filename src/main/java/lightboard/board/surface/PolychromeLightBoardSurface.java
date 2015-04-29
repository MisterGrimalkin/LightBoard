package lightboard.board.surface;

import lightboard.board.PolychromeLightBoard;
import lightboard.util.Sync;

import java.util.Timer;
import java.util.TimerTask;

public class PolychromeLightBoardSurface extends MonochromeLightBoardSurface {

    private final double[][][] ledPolyValue;

    private final PolychromeLightBoard[] boards;

    public PolychromeLightBoardSurface(PolychromeLightBoard... boards) {
        super(boards);
        this.boards = boards;
        ledPolyValue = new double[3][getRows()][getCols()];
    }


    ///////////////////
    // Light Control //
    ///////////////////

    @Override
    public LightBoardSurface init() {
        System.out.println("Starting PolychromeLightBoard Surface....");
        for (final PolychromeLightBoard board : boards) {
            Sync.addTask(new Sync.Task(board.getRefreshInterval()) {
                @Override
                public void runTask() {
                    board.dump(ledPolyValue);
                }
            });
        }
        System.out.println("Surface Active");
        return this;
    }

    @Override
    public boolean isOn(int x, int y) {
        return pointInRegion(x, y, boardRegion) && (ledPolyValue[0][y][x]>0 || ledPolyValue[0][y][x]>0 || ledPolyValue[0][y][x]>0);
    }

    @Override
    public boolean drawPoint(int x, int y, double value, Region r) {
        return drawPoint(x, y, value, value, value, r);
    }

    public boolean drawPoint(int x, int y, double red, double green, double blue, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledPolyValue[0][y][x] = Math.max(Math.min(red, 1.0), 0.0);
            ledPolyValue[1][y][x] = Math.max(Math.min(green, 1.0), 0.0);
            ledPolyValue[2][y][x] = Math.max(Math.min(blue, 1.0), 0.0);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean drawPattern(int xPos, int yPos, double[][][] chr, boolean clearBackground, Region r) {
        boolean changed = false;
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

}
