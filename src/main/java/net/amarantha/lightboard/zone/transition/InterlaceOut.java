package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Pattern;

public class InterlaceOut extends AbstractTransition {

    private int shift;
    private int shiftDelta;
    private int shiftDelta2;

    @Override
    public void reset() {
        shift = 0;
        shiftDelta = 5;
        shiftDelta2 = 1;
    }

    @Override
    public int getNumberOfSteps() {
        return zone.getWidth() / shiftDelta;
    }

    @Override
    public void animate(double progress) {
        zone.clear();
        Pattern pattern = new Pattern(zone.getPattern().getHeight(), zone.getPattern().getWidth() + (shift*2));
        boolean goLeft = true;
        for ( int r = 0; r < zone.getPattern().getHeight(); r++ ) {
            for ( int c = 0; c < zone.getPattern().getWidth(); c++ ) {
                pattern.drawPoint(r, c + (goLeft?0:shift*2), zone.getPattern().getColourPoint(r,c));
            }
            goLeft = !goLeft;
        }
        zone.drawPattern(zone.getRestX() - shift, zone.getRestY(), pattern);
        shift += shiftDelta2++;
    }

}
