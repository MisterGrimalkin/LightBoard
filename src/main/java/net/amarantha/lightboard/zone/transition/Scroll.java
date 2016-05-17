package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Edge;

public abstract class Scroll extends AbstractTransition {

    @Override
    public void tick() {
        zone.clear();
        x += deltaX;
        y += deltaY;
        if ( isComplete() ) {
            complete();
        } else {
            zone.drawPattern((int)Math.round(x), (int)Math.round(y), zone.getPattern());
        }
    }

    protected double x;
    protected double y;
    protected double deltaX;
    protected double deltaY;

    protected Edge edge = Edge.NO_SCROLL;

    public Scroll setEdge(Edge edge) {
        this.edge = edge;
        return this;
    }

    protected abstract boolean isComplete();

}
