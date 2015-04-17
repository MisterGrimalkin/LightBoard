package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.PolychromeLightBoard;
import lightboard.board.impl.BlankBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.TextBoard;
import lightboard.board.surface.LightBoardSurface;

import static lightboard.board.zone.Zones.*;

public class Main extends Application {

    private final static int COLS = 180;
    private final static int ROWS = 16;

    private static int ledRadius = 2;
    private static int ledSpacer = 0;

    private final static int CLOCK_WIDTH = 28;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        int cols = getCols();
        int rows = getRows();

        LightBoard board;

        String boardType = getParameters().getNamed().get("board");
        if ("graphical".equals(boardType)) {
            board = new GraphicalBoard(rows, cols, primaryStage, "Travel Board", ledRadius, ledSpacer).debugTo(new TextBoard(rows, cols));
        } else if ("text".equals(boardType)) {
            board = new TextBoard(rows, cols);
        } else {
            board = new BlankBoard(rows, cols);
        }
        board.init();

        LightBoardSurface surface = new LightBoardSurface(board);
        surface.init();

        startClock              (surface, COLS - CLOCK_WIDTH, 0,    CLOCK_WIDTH, ROWS);
        startBusStopDisplay     (surface, 0, 0,                     COLS - CLOCK_WIDTH, ROWS/2);
        startTubeStatusDisplay  (surface, 0, ROWS/2,                COLS - CLOCK_WIDTH, ROWS/2,         "bad");

    }

    private int getCols() {
        int cols = COLS;
        String colsStr = getParameters().getNamed().get("cols");
        try {
            cols = Integer.parseInt(colsStr);
        } catch ( NumberFormatException e ) {}
        return cols;
    }

    private int getRows() {
        int rows = ROWS;
        String rowsStr = getParameters().getNamed().get("rows");
        try {
            rows = Integer.parseInt(rowsStr);
        } catch ( NumberFormatException e ) {}
        return rows;
    }

}
