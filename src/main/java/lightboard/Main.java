package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.PolychromeLightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.TextBoard;
import lightboard.board.surface.PolychromeLightBoardSurface;
import lightboard.board.zone.impl.ImageZone;

import static lightboard.board.zone.Zones.*;

public class Main extends Application {

    private final static int COLS = 180;
    private final static int ROWS = 16;

    private final static int CLOCK_WIDTH = 28;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        int cols = COLS;
        int rows = 200;

        PolychromeLightBoard board = new GraphicalBoard(rows, cols, primaryStage, "Travel Board", 2, 1).debugTo(new TextBoard(rows, cols));
        board.init();

        PolychromeLightBoardSurface surface = new PolychromeLightBoardSurface(board);
        surface.init();

        ImageZone zone = new ImageZone(surface);
        zone.region(0, 16, cols, rows-16);
        zone.loadImage("test-image.jpg");
        zone.start();

        startClock              (surface, COLS - CLOCK_WIDTH, 0,    CLOCK_WIDTH, ROWS);
        startBusStopDisplay     (surface, 0, 0,                     COLS - CLOCK_WIDTH, ROWS/2);
        startTubeStatusDisplay  (surface, 0, ROWS/2,                COLS - CLOCK_WIDTH, ROWS/2,         "bad");

    }


}
