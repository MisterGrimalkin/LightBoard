package net.amarantha.lightboard.utility;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.scene.impl.ImageBanner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Singleton
public class LightBoardProperties extends PropertyManager {

    @Inject PropertyManager props;

    public boolean showTubeSummary() {
        return props.getString("showTubeSummary", "true").equals("true");
    }

    public boolean showTubeFullDetails() {
        return props.getString("showTubeFullDetails", "true").equals("true");
    }

    String ip = null;

    public String getIp() {
        if ( ip==null ) {
            StringBuilder output = new StringBuilder();

            Process p;
            try {
                p = Runtime.getRuntime().exec("sh getip.sh");
                p.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = reader.readLine())!= null) {
                    output.append(line).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ip = output.toString();
        }
        return ip;
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
        String className = props.getString("boardClass", "net.amarantha.lightboard.board.old.TextBoard");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return TextBoard.class;

    }

    public Class getImageBannerClass() {
        String className = props.getString("imageBannerClass", "net.amarantha.lightboard.scene.old.ImageBanner");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return ImageBanner.class;
    }

}
