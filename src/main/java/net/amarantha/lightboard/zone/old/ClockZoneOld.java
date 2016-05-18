package net.amarantha.lightboard.zone.old;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockZoneOld extends TextZone_Old {

    @Inject
    public ClockZoneOld(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        setScrollTick(500);
        setRestDuration(0);
        setRestPosition(AlignH.CENTRE, AlignV.MIDDLE);
        setFont(new SmallFont());
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
        String time = "{yellow}"+sdf.format(new Date());
        String day = "{yellow}"+new SimpleDateFormat("EEE").format(new Date());

        Pattern timePattern = getFont().renderString(time);
        Pattern dayPattern = getFont().renderString(day);

        int timeX = (region.width-timePattern.getWidth())/2;
        int dayX = (region.width-dayPattern.getWidth())/2;


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
