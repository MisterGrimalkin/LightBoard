package net.amarantha.lightboard.zone.transition;

public class ScrollIn extends ScrollTransition {

    @Override
    public void reset() {
        System.out.println("Steps="+getSteps());
        switch (edge) {
            case LEFT:
                x = zone.getRegion().left - zone.getPattern().getWidth();
                y = zone.getRestY();
                deltaX = (zone.getRestX() - x) / getSteps();
                deltaY = 0;
                break;
            case RIGHT:
                x = zone.getRegion().right;
                y = zone.getRestY();
                deltaX = -1 * ((x - zone.getRestX()) / getSteps());
                deltaY = 0;
                break;
            case TOP:
                x = zone.getRestX();
                y = zone.getRegion().top - zone.getPattern().getHeight();
                deltaX = 0;
                deltaY = (zone.getRestY() - y) / getSteps();
                break;
            case BOTTOM:
                x = zone.getRestX();
                y = zone.getRegion().bottom;
                deltaX = 0;
                deltaY = -1 * ((y - zone.getRestY()) / getSteps());
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
