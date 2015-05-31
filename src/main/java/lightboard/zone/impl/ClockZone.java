package lightboard.zone.impl;

import lightboard.font.Font;
import lightboard.font.SmallFont;
import lightboard.surface.LightBoardSurface;
import lightboard.util.MessageQueue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockZone extends TextZone {

    public ClockZone(LightBoardSurface surface) {
        super(surface, MessageQueue.Edge.NO_SCROLL, MessageQueue.Edge.NO_SCROLL, 500, new SmallFont());
        setScrollTick(500);
        setRestDuration(0);
        setRestPosition(MessageQueue.HPosition.CENTRE, MessageQueue.VPosition.MIDDLE);
    }

    public ClockZone(LightBoardSurface surface, MessageQueue.Edge scrollFrom, MessageQueue.Edge scrollTo, int restDuration, Font font) {
        super(surface, scrollFrom, scrollTo, restDuration, font);
    }

    private boolean colon = false;

    @Override
    public boolean render() {
        boolean drawn = false;
        SimpleDateFormat sdf;
        if ( colon ) {
            sdf = new SimpleDateFormat("HH:mm");
        } else {
            sdf = new SimpleDateFormat("HH mm");
        }
        colon = !colon;
        String time = "{green}"+sdf.format(new Date());
        String day = "{green}"+new SimpleDateFormat("EEE").format(new Date());

        if ( isPoly() ) {
            drawn |= drawPattern(2, 0, getFont().renderString(time).getColourValues(), true);
            drawn |= drawPattern(2, 7, getFont().renderString(day).getColourValues(), true);
            if (day.equalsIgnoreCase("Mon")) {
                drawn |= drawPattern(getFont().getStringWidth(day) + 4, 7, BIN_ICON);
            }
        } else {
            drawn |= drawPattern(2, 0, getFont().renderString(time).getBinaryValues(), true);
            drawn |= drawPattern(2, 7, getFont().renderString(day).getBinaryValues(), true);
            if (day.equalsIgnoreCase("Mon")) {
                drawn |= drawPattern(getFont().getStringWidth(day) + 4, 7, BIN_ICON);
            }
        }

        return drawn;
    }

    @Override
    public int getContentWidth() {
        return 20;
    }

    @Override
    public int getContentHeight() {
        return 11;
    }

    private static final boolean o = false;
    private static final boolean i = true;

    private static final boolean[][] BIN_ICON =
            {{i,i,i,i},
             {i,i,i,i},
             {i,o,o,i},
             {i,o,o,i},
             {i,o,o,i},
             {o,i,i,i}};


}
