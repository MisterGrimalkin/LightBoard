package net.amarantha.lightboard.zone.transition;

public class ExplodeOut extends Explode {

    @Override
    public void reset() {
        super.reset();
        spacing = 0;
    }

    @Override
    protected boolean isComplete() {
        return spacing > maxSpacing;
    }

    @Override
    protected void updateSpacing() {
        spacing++;
    }

}
