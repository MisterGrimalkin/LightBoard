package net.amarantha.lightboard.zone.transition;

public class ScrollOut extends Scroll {

    @Override
    public void reset() {
        long steps = getDuration() / zone.getTick();
        x = zone.getRestX();
        y = zone.getRestY();
        switch (edge) {
            case LEFT:
                deltaX = Math.floor((x + zone.getPattern().getWidth()) / -steps);
                deltaY = 0;
                break;
            case RIGHT:
                deltaX = Math.ceil((zone.getWidth() - x) / steps);
                deltaY = 0;
                break;
            case TOP:
                deltaX = 0;
                deltaY = Math.floor((y + zone.getPattern().getHeight()) / -steps);
                break;
            case BOTTOM:
                deltaX = 0;
                deltaY = Math.ceil((y + zone.getPattern().getHeight()) / steps);
                break;
            case NO_SCROLL:
                break;
        }
    }

    @Override
    protected boolean isComplete() {
        switch (edge){
            case LEFT:
                return x <= -1 * zone.getPattern().getWidth();
            case RIGHT:
                return x >= zone.getRight();
            case TOP:
                return y <= -1 * zone.getPattern().getHeight();
            case BOTTOM:
                return y >= zone.getBottom();
            case NO_SCROLL:
                break;
        }
        return true;
    }

}
