package net.amarantha.lightboard.zone.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.LargeFont;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Now;
import net.amarantha.lightboard.utility.Sync;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownZone extends TextZone {

    @Inject private Now now;

    @Inject
    public CountdownZone(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        setScrollTick(500);
        setRestDuration(0);
        setRestPosition(AlignH.CENTRE, AlignV.MIDDLE);
        setFont(new SimpleFont());
//        scrollLeft();
    }

    private boolean on = false;

    @Override
    public boolean render() {
        if ( !paused ) {
            String message;
            if (new SimpleDateFormat("YYYY").format(now.now()).equals("2016")) {
                if (on) {
                    message = "HAPPY NEW YEAR!!!!!\n2016 Will Be Awesome :-)";
                } else {
                    message = "";
                }
                on = !on;
            } else {
                message = timeUntil("22:45");
                if (message == null) {
                    if (on) {
                        message = "Leave The Fucking House!!!";
                    } else {
                        message = "";
                    }
                    on = !on;
                }
            }
            Pattern text = getFont().renderString(message, AlignH.CENTRE);
            return drawPattern((region.width - text.getCols()) / 2, (region.height - text.getRows()) / 2, text);

        }
        return false;
    }

    private String timeUntil(String time) {
        try {

            Date leaveTime = Now.longTimeOnly(new SimpleDateFormat("HH:mm").parse(time));
            long msLeft = leaveTime.getTime() - now.longTime().getTime();
            if ( msLeft > 0 ) {

                Date timeLeft = new Date(msLeft);
                long hours = Long.parseLong(new SimpleDateFormat("H").format(timeLeft));
                long mins = Long.parseLong(new SimpleDateFormat("m").format(timeLeft));
                long secs = Long.parseLong(new SimpleDateFormat("s").format(timeLeft));

                String text = "We are LEAVING THE HOUSE in\n "
                        + ( hours > 0 ? hours + " hr " : "")
                        + mins + " min "
                        + secs + " sec";

                return text;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getContentWidth() {
        return 192;
    }

    @Override
    public int getContentHeight() {
        return 32;
    }


}
