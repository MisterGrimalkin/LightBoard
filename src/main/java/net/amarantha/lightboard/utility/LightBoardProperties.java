package net.amarantha.lightboard.utility;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.TextBoard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Singleton
public class LightBoardProperties extends PropertyManager {

    private static boolean simulationMode;
    private static boolean withServer;
    private static boolean testMode;

    // static stuff for command line params
    public static void processArgs(String[] args) {
        List<String> params = Arrays.asList(args);
        simulationMode = params.contains("-simulation");
        testMode = params.contains("-test");
        withServer = !params.contains("-noserver");
    }

    public static boolean isSimulationMode() {
        return simulationMode;
    }

    public static boolean isWithServer() {
        return withServer;
    }

    public static boolean isTestMode() {
        return testMode;
    }

    @Inject PropertyManager props;

    private String ip = null;

    public String getIp() {
        if ( ip==null ) {
            StringBuilder output = new StringBuilder();

            Process p;
            try {
                p = Runtime.getRuntime().exec("sh scripts/getip.sh");
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
            ip = output.toString().trim();
        }
        return ip;
    }

    public int getBoardRows() {
        return props.getInt("boardRows", 32);
    }

    public int getBoardCols() {
        return props.getInt("boardCols", 192);
    }

    public Class getBoardClass() {
        if ( props==null ) {
            props = new PropertyManager();
        }
        String className = props.getString("boardClass", "net.amarantha.lightboard.board.TextBoard");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return TextBoard.class;

    }

    public String getTransitionPackage() {
        return getString("transitionPackage", "net.amarantha.lightboard.zone.transition");
    }

    public String getFontPackage() {
        return getString("fontPackage", "net.amarantha.lightboard.font");
    }

    public String getDefaultScene() {
        return getString("defaultScene", "splash");
    }

    public long getSceneTick() { return getInt("sceneTick", 5); }
}
