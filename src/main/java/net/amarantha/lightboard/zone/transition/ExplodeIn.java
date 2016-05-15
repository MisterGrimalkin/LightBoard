package net.amarantha.lightboard.zone.transition;

public class ExplodeIn extends Explode {

    @Override
    public void reset() {
        super.reset();
        spacing = maxSpacing;
    }

    @Override
    protected boolean isComplete() {
        return spacing < 0;
    }

    @Override
    protected void updateSpacing() {
        spacing--;
    }

}
