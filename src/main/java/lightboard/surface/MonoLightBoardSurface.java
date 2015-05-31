package lightboard.surface;

import lightboard.board.MonoLightBoard;
import lightboard.util.Sync;

public class MonoLightBoardSurface extends LightBoardSurface {

    private final double[][] ledValue;

    private final MonoLightBoard[] boards;

    public MonoLightBoardSurface(MonoLightBoard... boards) {
        super(boards);
        this.boards = boards;
        ledValue = new double[getRows()][getCols()];
    }


    ///////////////////
    // Light Control //
    ///////////////////

    @Override
    public LightBoardSurface init() {
        System.out.println("Starting MonochromeLightBoard Surface....");
        for (final MonoLightBoard board : boards) {
            Sync.addTask(new Sync.Task(board.getRefreshInterval()) {
                @Override
                public void runTask() {
                    board.dump(ledValue);
                }
            });
        }
        System.out.println("Surface Active");
        return this;
    }

    @Override
    public boolean isOn(int x, int y) {
        return pointInRegion(x, y, boardRegion) && ledValue[y][x]>0;
    }

    @Override
    public boolean drawPoint(int x, int y, Region r) {
        return drawPoint(x, y, 1.0, r);
    }

    @Override
    public boolean clearPoint(int x, int y, Region r) {
        return drawPoint(x, y, 0.0, r);
    }

    public boolean drawPoint(int x, int y, double value) {
        return drawPoint(x, y, value, boardRegion);
    }

    public boolean drawPoint(int x, int y, double value, Region r) {
        if ( pointInRegion(x, y, r) ) {
            ledValue[y][x] = Math.max(Math.min(value, 1.0), 0.0);
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
            for (int x = 0; x < getCols(); x++) {
                for (int y = 0; y < getRows(); y++) {
                    if ( x-xPos >= 0 && x-xPos < chr[0].length && y-yPos >= 0 && y-yPos < chr.length ) {
                        changed |= drawPoint(x, y, chr[y - yPos][x - xPos], r);
                    }
                }
            }
        }
        return changed;
    }

}
