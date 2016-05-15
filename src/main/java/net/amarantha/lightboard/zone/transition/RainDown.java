package net.amarantha.lightboard.zone.transition;

public class RainDown extends AbstractTransition {


    @Override
    public void reset() {
        delay = getDuration() / zone.getPattern().getHeight();
        currentRow = zone.getPattern().getHeight() - 1;
        lastDrawn = System.currentTimeMillis();
    }

    private long delay;
    private int currentRow ;

    private long lastDrawn;


    @Override
    public void tick() {
        if ( currentRow <= 0 ) {
            zone.clear();
            complete();
        } else if ( System.currentTimeMillis() - lastDrawn >= delay ) {
            zone.clear();
            for (int r = 0; r < zone.getRestY()+zone.getPattern().getHeight(); r++ ) {
                if ( r < (zone.getRestY()+currentRow) ) {
                    for (int c = 0; c < zone.getPattern().getWidth(); c++) {
                        if ( Math.random() <= 0.3 ) {
                            surface.drawPoint(c + zone.getRestX(), r, zone.getPattern().getColourPoint(currentRow, c), zone.getRegion());
                        }
                    }
                } else {
                    for (int c = 0; c < zone.getPattern().getWidth(); c++) {
                        surface.drawPoint(c+zone.getRestX(), r, zone.getPattern().getColourPoint(r-zone.getRestY(), c), zone.getRegion());
                    }
                }
            }
            lastDrawn = System.currentTimeMillis();
            currentRow--;
        }
    }
}
