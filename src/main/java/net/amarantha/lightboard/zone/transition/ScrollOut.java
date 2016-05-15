package net.amarantha.lightboard.zone.transition;

public class ScrollOut extends ScrollTransition {

    @Override
    public void reset() {
        x = zone.getRestX();
        y = zone.getRestY();
        switch (edge) {
            case LEFT:
                deltaX = -1 * ((x + zone.getPattern().getWidth()) / getSteps());
                deltaY = 0;
                break;
            case RIGHT:
                deltaX = (zone.getRegion().right - x) / getSteps();
                deltaY = 0;
                break;
            case TOP:
                deltaX = 0;
                deltaY = -1 * ((y + zone.getPattern().getHeight()) / getSteps());
                break;
            case BOTTOM:
                deltaX = 0;
                deltaY = (zone.getRegion().bottom - y) / getSteps();
                break;
            case NO_SCROLL:
                break;
        }
        deltaX = Math.round(deltaX);
        deltaY = Math.round(deltaY);
    }

    @Override
    protected boolean isComplete() {
        switch (edge){
            case LEFT:
                return x <= -1 * zone.getPattern().getWidth();
            case RIGHT:
                return x >= zone.getRegion().right;
            case TOP:
                return y <= -1 * zone.getPattern().getHeight();
            case BOTTOM:
                return y >= zone.getRegion().bottom;
            case NO_SCROLL:
                break;
        }
        return true;
    }

}
