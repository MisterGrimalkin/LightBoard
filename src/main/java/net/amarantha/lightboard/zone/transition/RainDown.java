package net.amarantha.lightboard.zone.transition;

public class RainDown extends AbstractTransition {

    @Override
    public void reset() {
        delay = getDuration() / zone.getPattern().getHeight();
        currentRow = zone.getPattern().getHeight() - 1;
        lastDrawn = System.currentTimeMillis();
        randomLimit = 0.6;
        minRandomLimit = 0.0;
        fallStart = 0;
        fallStartDelta = (zone.getRestY() / zone.getPattern().getHeight()) * 0.6;
        randomLimitDelta = ((randomLimit - minRandomLimit) / (zone.getPattern().getHeight())) * 1.1 ;
    }

    private long delay;
    private int currentRow ;

    private double fallStart;
    private double fallStartDelta;

    private long lastDrawn;

    private double randomLimit;
    private double minRandomLimit;
    private double randomLimitDelta;

    @Override
    public void tick() {
        if ( currentRow <= 0 ) {
            zone.clear();
            complete();
        } else if ( System.currentTimeMillis() - lastDrawn >= delay ) {
            zone.clear();
            for (int r = (int)fallStart; r < zone.getRestY()+zone.getPattern().getHeight(); r++ ) {
                if ( r < (zone.getRestY()+currentRow) ) {
                    for (int c = 0; c < zone.getPattern().getWidth(); c++) {
                        if ( Math.random() <= randomLimit) {
                            zone.drawPoint(c + zone.getRestX(), r, zone.getPattern().getColourPoint(currentRow, c));
                        }
                    }
                } else {
                    for (int c = 0; c < zone.getPattern().getWidth(); c++) {
                        zone.drawPoint(c+zone.getRestX(), r, zone.getPattern().getColourPoint(r-zone.getRestY(), c));
                    }
                }
            }
            lastDrawn = System.currentTimeMillis();
            if ( randomLimit >= minRandomLimit ) {
                randomLimit -= randomLimitDelta;
            }
            currentRow--;
            fallStart += fallStartDelta;
        }
    }

}
