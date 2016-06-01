package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Edge;

public abstract class Scroll extends AbstractTransition {

    @Override
    public void animate(double progress) {
        zone.clear();
        zone.drawPattern((int)Math.round(x), (int)Math.round(y), zone.getPattern());
        x += deltaX;
        y += deltaY;
    }

    @Override
    public int getNumberOfSteps() {
        return (int)(getDuration() / zone.getTick());
    }

    protected abstract boolean isComplete();

    protected double x;
    protected double y;
    protected double deltaX;
    protected double deltaY;

    protected Edge edge = Edge.NONE;

    public Scroll setEdge(Edge edge) {
        this.edge = edge;
        return this;
    }

}
