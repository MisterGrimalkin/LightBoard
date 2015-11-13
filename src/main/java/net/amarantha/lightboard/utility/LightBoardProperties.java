package net.amarantha.lightboard.utility;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LightBoardProperties extends PropertyManager {

    @Inject PropertyManager props;

    public boolean showTubeSummary() {
        return props.getString("showTubeSummary", "true").equals("true");
    }

    public boolean showTubeFullDetails() {
        return props.getString("showTubeFullDetails", "true").equals("true");
    }

    public String getIp() {
        return props.getString("ip", "127.0.0.1");
    }

}
