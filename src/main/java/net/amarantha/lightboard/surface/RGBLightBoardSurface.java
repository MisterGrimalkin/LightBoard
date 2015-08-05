package net.amarantha.lightboard.surface;

import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.util.Sync;

public class RGBLightBoardSurface extends LightBoardSurface {

    private final double[][][] ledPolyValue;

    private final LightBoard[] boards;

    public RGBLightBoardSurface(LightBoard... boards) {
        super(boards);
        this.boards = boards;
        ledPolyValue = new double[3][getRows()][getCols()];
    }


    ///////////////////
    // Light Control //
    ///////////////////

    @Override
    public LightBoardSurface init() {
        System.out.println("Starting ColourLightBoard Surface....");
        for (final LightBoard board : boards) {
            System.out.println(board.getUpdateInterval());
            Sync.addTask(new Sync.Task(board.getUpdateInterval()) {
                @Override
                public void runTask() {
                    board.update(ledPolyValue);
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
    public boolean drawPoint(int x, int y, Region r) {
        return drawPoint(x, y, 1.0, 1.0, 1.0, r);
    }

    @Override
    public boolean clearPoint(int x, int y, Region r) {
        return drawPoint(x, y, 0.0, 0.0, 0.0, r);
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

    @Override
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

}
