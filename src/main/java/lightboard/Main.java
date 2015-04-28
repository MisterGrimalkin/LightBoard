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
import static lightboard.board.zone.Zones.startTubeStatusDisplay;

public class Main extends Application {

    private final static int COLS = 90;
    private final static int ROWS = 16;

    private static int ledRadius = 2;
    private static int ledSpacer = 1;

    private final static int CLOCK_WIDTH = 28;

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

        LightBoard board;

        String boardType = getParameters().getNamed().get("board");
        if ("graphical".equals(boardType)) {
            board = new GraphicalBoard(rows, cols, primaryStage, "Travel Board", ledRadius, ledSpacer).debugTo(new TextBoard(rows, cols));
            ((GraphicalBoard)board).setServer(server);
        } else if ("text".equals(boardType)) {
            board = new TextBoard(rows, cols);
        } else {
            board = RaspberryPiLightBoard.makeBoard1();
        }
        board.init();

//        LightBoard board2 = new GraphicalBoard(rows, cols, new Stage(), "Travel Board", ledRadius, ledSpacer).debugTo(new TextBoard(rows, cols));
//        board2.init();

        LightBoardSurface surface = new LightBoardSurface(board);
        surface.init();
//        LightBoardSurface surface2 = new LightBoardSurface(board2);

//        CompositeSurface cSurface =
//                new CompositeSurface(ROWS, COLS*2)
//                .addSurface(surface, 0, 0)
//                .addSurface(surface2, COLS, 0);
//        cSurface.init();

//        TextZone clockZone = startClock          (surface, 0, 0,    COLS, ROWS/2);

        TextZone busZone =  startBusStopDisplay(surface, 0, 0, COLS, ROWS/2);
        TextZone tubeZone = startTubeStatusDisplay(surface, 0, ROWS/2, COLS, ROWS/2,         "bad");

        if ( server!=null ) {
            MessageUpdater m = new MessageUpdater(busZone, tubeZone);
            MessageResource.bindUpdater(m);
        }

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
