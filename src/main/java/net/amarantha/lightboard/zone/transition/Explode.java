package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Pattern;

public abstract class Explode extends AbstractTransition {

    protected int spacing = 0;

    protected int maxSpacing = 30;

    private long delay;

    private long lastDrawn;

    @Override
    public void reset() {
        delay = getDuration() / maxSpacing;
        lastDrawn = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        if ( isComplete() ) {
            complete();
        } else if ( System.currentTimeMillis() - lastDrawn >= delay ) {
            int xSpacing = spacing;
            int ySpacing = spacing;
            Pattern exploded = new Pattern(zone.getPattern().getHeight() * (ySpacing + 1), zone.getPattern().getWidth() * (xSpacing + 1));
            for ( int r=0; r<zone.getPattern().getHeight(); r++ ) {
                for ( int c=0; c<zone.getPattern().getWidth(); c++ ) {
                    int xJitter = 0;//(int)Math.round((Math.random()-0.5) * (spacing/3));
                    int yJitter = 0;//(int)Math.round((Math.random()-0.5) * (spacing/3));
                    exploded.drawPoint(r*(ySpacing+1)+xJitter, c*(xSpacing+1)+yJitter, zone.getPattern().getColourPoint(r, c));
                }
            }
            int explodedX = zone.getRestX() + (zone.getPattern().getWidth()/2) - (exploded.getWidth()/2);
            int explodedY = zone.getRestY() + (zone.getPattern().getHeight()/2) - (exploded.getHeight()/2);
            zone.clear();
            surface.drawPattern(explodedX, explodedY, exploded, zone.getRegion());
            lastDrawn = System.currentTimeMillis();
            updateSpacing();
        }
    }

    protected abstract boolean isComplete();

    protected abstract void updateSpacing();

}
