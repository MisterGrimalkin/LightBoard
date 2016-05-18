package net.amarantha.lightboard.zone.transition;

public class ScrollIn extends Scroll {

    @Override
    public void reset() {
        long steps = getNumberOfSteps();
        switch (edge) {
            case LEFT:
                x = -zone.getPattern().getWidth();
                y = zone.getRestY();
                deltaX = Math.ceil((zone.getRestX() - x) / steps);
                deltaY = 0;
                break;
            case RIGHT:
                x = zone.getWidth();
                y = zone.getRestY();
                deltaX = Math.floor((x - zone.getRestX()) / -steps);
                deltaY = 0;
                break;
            case TOP:
                x = zone.getRestX();
                y = -zone.getPattern().getHeight();
                deltaX = 0;
                deltaY = Math.ceil((zone.getRestY() - y) / steps);
                break;
            case BOTTOM:
                x = zone.getRestX();
                y = zone.getHeight();
                deltaX = 0;
                deltaY = Math.floor((y - zone.getRestY()) / -steps);
                break;
            case NO_SCROLL:
                break;
        }
    }

    @Override
    protected boolean isComplete() {
        switch (edge){
            case LEFT:
                return x >= zone.getRestX();
            case RIGHT:
                return x <= zone.getRestX();
            case TOP:
                return y >= zone.getRestY();
            case BOTTOM:
                return y <= zone.getRestY();
            case NO_SCROLL:
                break;
        }
        return true;
    }

}
