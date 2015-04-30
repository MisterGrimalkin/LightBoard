package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.RaspberryPiLightBoard;
import lightboard.board.impl.TextBoard;
import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.TextZone;
import lightboard.updater.WebService;
import lightboard.updater.schedule.MessageResource;
import lightboard.updater.schedule.MessageUpdater;
import lightboard.util.Sync;
import org.glassfish.grizzly.http.server.HttpServer;

import static lightboard.board.zone.Zones.*;

public class Main extends Application {

    private final static int COLS = 180;
    private final static int ROWS = 16;

    private final static int CLOCK_WIDTH = 30;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        System.out.println("Starting Up....");

        HttpServer server = WebService.start(getParameters().getNamed().get("ip"));

        LightBoard board;
        switch ( getBoardType() ) {
            case RASPBERRY_PI:
                board = new RaspberryPiLightBoard(ROWS, COLS);
                break;
            case GRAPHICAL:
                board = new GraphicalBoard(ROWS, COLS, primaryStage);
                break;
            case TEXT:
            default:
                board = new TextBoard(ROWS, COLS);
                break;
        }
        board.init();

        LightBoardSurface surface = new LightBoardSurface(board);
        surface.init();

        TextZone clockZone = startClock          (surface, COLS-CLOCK_WIDTH, 0,    CLOCK_WIDTH, ROWS);

        TextZone busZone =  startBusStopDisplay(surface, 0, 0, COLS-CLOCK_WIDTH, ROWS/2);
        TextZone tubeZone = startTubeStatusDisplay(surface, 0, ROWS/2, COLS-CLOCK_WIDTH, ROWS/2,         "bad");

        if ( server!=null ) {
            MessageUpdater m = new MessageUpdater(busZone, tubeZone);
            MessageResource.bindUpdater(m);
        }

        Sync.start();

    }

    private BoardType getBoardType() {
        String boardType = getParameters().getNamed().get("board");
        if ("graphical".equals(boardType)) {
            return BoardType.GRAPHICAL;
        } else if ("text".equals(boardType)) {
            return BoardType.TEXT;
        } else {
            return BoardType.RASPBERRY_PI;
        }
    }

    public enum BoardType { TEXT, GRAPHICAL, RASPBERRY_PI }

}
