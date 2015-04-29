package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.RaspberryPiLightBoard;
import lightboard.board.impl.TextBoard;
import lightboard.board.surface.CompositeSurface;
import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.TextZone;
import lightboard.updater.schedule.MessageResource;
import lightboard.updater.schedule.MessageUpdater;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.InetAddress;
import java.net.URI;

import static lightboard.board.zone.Zones.startBusStopDisplay;
import static lightboard.board.zone.Zones.startClock;
import static lightboard.board.zone.Zones.startTubeStatusDisplay;

public class Main extends Application {

    private final static int COLS = 90;
    private final static int ROWS = 16;

    private static int ledRadius = 2;
    private static int ledSpacer = 0;

    private final static int CLOCK_WIDTH = 30;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        HttpServer server = startServer();

        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println(addr.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int cols = COLS;
        int rows = ROWS;

        LightBoard board1;
        LightBoard board2;

        String boardType = getParameters().getNamed().get("board");
        if ("graphical".equals(boardType)) {
            board1 = new GraphicalBoard(rows, cols, primaryStage, "Travel Board", ledRadius, ledSpacer).debugTo(new TextBoard(rows, cols));
            ((GraphicalBoard)board1).setServer(server);
            board2 = new GraphicalBoard(rows, cols, new Stage(), "Travel Board", ledRadius, ledSpacer).debugTo(new TextBoard(rows, cols));
        } else if ("text".equals(boardType)) {
            board1 = new TextBoard(rows, cols);
            board2 = new TextBoard(rows, cols);
        } else {
            board1 = RaspberryPiLightBoard.makeBoard1();
            board2 = RaspberryPiLightBoard.makeBoard2();
        }
        board1.init();
        board2.init();

        LightBoardSurface surface1 = new LightBoardSurface(board1);
        LightBoardSurface surface2 = new LightBoardSurface(board2);

        CompositeSurface cSurface =
                new CompositeSurface(ROWS, COLS*2)
                .addSurface(surface1, 0, 0)
                .addSurface(surface2, COLS, 0);
        cSurface.init();

        TextZone clockZone = startClock          (cSurface, (COLS*2)-CLOCK_WIDTH, 0,    CLOCK_WIDTH, ROWS);

        TextZone busZone =  startBusStopDisplay(cSurface, 0, 0, (COLS*2)-CLOCK_WIDTH, ROWS/2);
        TextZone tubeZone = startTubeStatusDisplay(cSurface, 0, ROWS/2, (COLS*2)-CLOCK_WIDTH, ROWS/2,         "bad");

        if ( server!=null ) {
            MessageUpdater m = new MessageUpdater(busZone, tubeZone);
            MessageResource.bindUpdater(m);
        }

        Sync.start();

    }

    private String getIp() {
        String ip = getParameters().getNamed().get("ip");
        if ( ip==null || ip.isEmpty() ) {
            ip = "192.168.0.20";
        }
        return "http://"+ip+":8080/lightboard/";
    }

    public HttpServer startServer() {
        String ip = getParameters().getNamed().get("ip");

        if ( ip!=null && !ip.isEmpty() ) {
            final ResourceConfig rc = new ResourceConfig().packages("lightboard");
            return GrizzlyHttpServerFactory.createHttpServer(URI.create(getIp()), rc);
        }
        return null;
    }

}
