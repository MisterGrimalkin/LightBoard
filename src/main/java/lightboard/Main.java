package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.LightBoardSurface;
import lightboard.board.impl.TextBoard;
import static lightboard.zone.Zones.*;

public class Main extends Application {

    private final static int COLS = 180;
    private final static int ROWS = 16;

    private final static int CLOCK_WIDTH = 28;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        LightBoard board = new GraphicalBoard(ROWS, COLS, primaryStage, "Travel Board", 2, 0).debugTo(new TextBoard(ROWS, COLS));
        board.init();

        LightBoardSurface surface = new LightBoardSurface(board);
        surface.init();

        startClock              (surface, COLS - CLOCK_WIDTH, 0,    CLOCK_WIDTH, ROWS);
        startBusStopDisplay     (surface, 0, 0,                     COLS - CLOCK_WIDTH, ROWS/2);
        startTubeStatusDisplay  (surface, 0, ROWS/2,                COLS - CLOCK_WIDTH, ROWS/2,         "victoria");

    }


}
