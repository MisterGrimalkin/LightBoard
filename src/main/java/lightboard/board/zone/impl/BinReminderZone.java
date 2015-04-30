package lightboard.board.zone.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.LightBoardZone;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BinReminderZone extends LightBoardZone {

    public BinReminderZone(LightBoardSurface surface) {
        super(surface);
    }

    @Override
    public boolean render() {
        Date d = new Date();
//        int minute = Integer.parseInt(new SimpleDateFormat("mm").format(d));
        String day = new SimpleDateFormat("EEEE").format(d);
//        if ( minute % 2 == 0 ) {
        if ( day.equalsIgnoreCase("Monday") ) {
            return drawPattern(0,0,BIN_ICON);
        }
        return false;
    }

    @Override
    public int getContentWidth() {
        return BIN_ICON[0].length;
    }

    @Override
    public int getContentHeight() {
        return BIN_ICON.length;
    }

    private static final boolean o = false;
    private static final boolean i = true;

    private static final boolean[][] BIN_ICON =
            {{o,i,i,o},
             {i,i,i,i},
             {i,o,o,i},
             {i,o,o,i},
             {o,i,o,i},
             {o,i,i,i}};

}
