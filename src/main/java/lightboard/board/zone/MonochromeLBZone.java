package lightboard.board.zone;

import lightboard.board.surface.MonochromeLightBoardSurface;

public abstract class MonochromeLBZone extends LBZone{

    private final MonochromeLightBoardSurface surface;

    protected MonochromeLBZone(MonochromeLightBoardSurface surface) {
        super(surface);
        this.surface = surface;
    }


    /////////////////////
    // Surface Drawing //
    /////////////////////

    protected boolean drawPoint(int x, int y, double value) {
        return surface.drawPoint(region.left+contentLeft+x, region.top+contentTop+y, value, region);
    }

    protected boolean clearPoint(int x, int y) {
        return drawPoint(x, y, 0.0);
    }

    protected boolean drawPattern(int x, int y, double[][] pattern) {
        return surface.drawPattern(region.left+contentLeft+x, region.top+contentTop+y, pattern, region);
    }

    protected boolean drawPattern(int x, int y, double[][] pattern, boolean clearBackground) {
        return surface.drawPattern(region.left+contentLeft+x, region.top+contentTop+y, pattern, clearBackground, region);
    }


}
