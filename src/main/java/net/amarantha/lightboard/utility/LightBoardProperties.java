package net.amarantha.lightboard.utility;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.scene.impl.ImageBanner;

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

    public int getBoardRows() {
        return props.getInt("boardRows", 32);
    }

    public int getBoardCols() {
        return props.getInt("boardCols", 192);
    }

    public int getBannerIntervalSeconds() {
        return props.getInt("bannerInterval", 60) * 1000;
    }

    public Class getBoardClass() {
        if ( props==null ) {
            props = new PropertyManager();
        }
        String className = props.getString("boardClass", "net.amarantha.lightboard.board.impl.TextBoard");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return TextBoard.class;

    }

    public Class getImageBannerClass() {
        String className = props.getString("imageBannerClass", "net.amarantha.lightboard.scene.impl.ImageBanner");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return ImageBanner.class;
    }

}
