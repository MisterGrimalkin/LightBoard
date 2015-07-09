package net.amarantha.lightboard.zone.impl;

import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.entity.HPosition;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.entity.VPosition;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.surface.LightBoardSurface;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockZone extends TextZone {

    public ClockZone(LightBoardSurface surface) {
        super(surface, Edge.NO_SCROLL, Edge.NO_SCROLL, 500, new SmallFont());
        setScrollTick(500);
        setRestDuration(0);
        setRestPosition(HPosition.CENTRE, VPosition.MIDDLE);
    }

    public ClockZone(LightBoardSurface surface, Edge scrollFrom, Edge scrollTo, int restDuration, Font font) {
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

        Pattern timePattern = getFont().renderString(time);
        Pattern dayPattern = getFont().renderString(day);

        int timeX = (region.width-timePattern.getCols())/2;
        int dayX = (region.width-dayPattern.getCols())/2;


        drawn |= drawPattern(timeX, 0, timePattern, true);
        drawn |= drawPattern(dayX, 7, dayPattern, true);

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
