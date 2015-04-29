package lightboard.board.surface;

import java.util.ArrayList;
import java.util.List;

public class CompositeSurface extends LightBoardSurface {

    private final int rows;
    private final int cols;

    private List<InnerSurface> innerSurfaces = new ArrayList<>();

    public CompositeSurface(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        boardRegion = safeRegion(0,0,cols,rows);
    }

    public CompositeSurface addSurface(LightBoardSurface surface, int x, int y) {
        innerSurfaces.add(new InnerSurface(surface, x, y));
        return this;
    }

    @Override
    public boolean isOn(int x, int y) {
        InnerSurface s = getInnerSurface(x, y);
        if ( s!=null ) {
            return s.surface.isOn(s.relativeX(x), s.relativeY(y));
        }
        return false;
    }

    @Override
    public boolean drawPoint(int x, int y, Region r) {
        if ( pointInRegion(x, y, r) ) {
            InnerSurface s = getInnerSurface(x, y);
            if (s != null) {
                return s.surface.drawPoint(s.relativeX(x), s.relativeY(y), s.relativeRegion(r));
            }
        }
        return false;
    }

    @Override
    public boolean clearPoint(int x, int y, Region r) {
        if ( pointInRegion(x, y, r) ) {
            InnerSurface s = getInnerSurface(x, y);
            if (s != null) {
                return s.surface.clearPoint(s.relativeX(x), s.relativeY(y), s.relativeRegion(r));
            }
        }
        return false;
    }

    @Override
    public boolean clearSurface() {
        boolean drawn = false;
        for ( InnerSurface s : innerSurfaces ) {
            drawn |= s.surface.clearSurface();
        }
        return drawn;
    }

    @Override
    public LightBoardSurface init() {
        for ( InnerSurface s : innerSurfaces ) {
            s.surface.init();
        }
        return this;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    private InnerSurface getInnerSurface(int x, int y) {
        for ( InnerSurface s : innerSurfaces ) {
            if ( s.isOnSurface(x, y) ) {
                return s;
            }
        }
        return null;
    }

    private static class InnerSurface {
        final LightBoardSurface surface;
        final int surfaceX;
        final int surfaceY;
        final int surfaceCols;
        final int surfaceRows;
        public InnerSurface(LightBoardSurface surface, int surfaceX, int surfaceY) {
            this.surface = surface;
            this.surfaceX = surfaceX;
            this.surfaceY = surfaceY;
            this.surfaceCols = surface.getCols();
            this.surfaceRows = surface.getRows();
        }
        boolean isOnSurface(int x, int y) {
            return x >= surfaceX && x < ( surfaceX+surfaceCols )
                    && y >= surfaceY && y < ( surfaceY+surfaceRows );
        }
        int relativeX(int x) {
            return x-surfaceX;
        }
        int relativeY(int y) {
            return y-surfaceY;
        }
        Region relativeRegion(Region r) {
            return new Region(r.left-surfaceX, r.top-surfaceY, r.width, r.height);
        }
    }


}
