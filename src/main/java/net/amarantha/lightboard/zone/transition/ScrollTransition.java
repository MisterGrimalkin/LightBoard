package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Edge;

public abstract class ScrollTransition extends AbstractTransition {

    @Override
    public void tick() {
        surface.clearRegion(zone.getRegion());
        x += deltaX;
        y += deltaY;
        if ( isComplete() ) {
            complete();
        } else {
            surface.drawPattern((int)Math.round(x), (int)Math.round(y), zone.getPattern());
        }
    }

    protected double x;
    protected double y;
    protected double deltaX;
    protected double deltaY;

    protected Edge edge = Edge.NO_SCROLL;

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    protected abstract boolean isComplete();

}
