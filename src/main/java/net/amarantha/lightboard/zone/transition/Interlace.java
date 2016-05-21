package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Pattern;

public abstract class Interlace extends AbstractTransition {

    protected int shift;
    protected int maxShift = 300;
    protected int shiftDelta = 4;

    @Override
    public int getNumberOfSteps() {
        return maxShift * shiftDelta;
    }

    public abstract void updateShift();

    public abstract boolean isComplete();

    @Override
    public void animate(double progress) {
        if ( isComplete() ) {
            zone.clear();
            complete();
        } else {
            zone.clear();
            Pattern pattern = new Pattern(zone.getPattern().getHeight(), zone.getPattern().getWidth() + (Math.abs(shift) * 2));
            boolean goLeft = true;
            for (int r = 0; r < zone.getPattern().getHeight(); r++) {
                for (int c = 0; c < zone.getPattern().getWidth(); c++) {
                    pattern.drawPoint(r, c + (goLeft ? 0 : Math.abs(shift) * 2), zone.getPattern().getColourPoint(r, c));
                }
                goLeft = !goLeft;
            }
            zone.drawPattern(zone.getRestX() - Math.abs(shift), zone.getRestY(), pattern);
            updateShift();
        }
    }

}
