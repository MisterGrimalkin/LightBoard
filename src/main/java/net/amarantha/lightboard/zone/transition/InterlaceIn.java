package net.amarantha.lightboard.zone.transition;

public class InterlaceIn extends Interlace {

    @Override
    public void updateShift() {
        shift -= shiftDelta--;
    }

    @Override
    public boolean isComplete() {
        return shift <= 0;
    }

    @Override
    public void reset() {
        shift = maxShift;
        shiftDelta = -4;
    }

}
