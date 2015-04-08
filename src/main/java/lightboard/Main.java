package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.GrayscaleLightBoard;
import lightboard.board.GrayscaleLightBoardSurface;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.TextBoard;
import lightboard.zone.impl.ImageZone;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private final static int COLS = 390;
    private final static int ROWS = 195;

    private final static int CLOCK_WIDTH = 28;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("sb.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rows = Math.min(image.getHeight(), 500);
        int cols = Math.min(image.getWidth(), 500);

        GrayscaleLightBoard board = new GraphicalBoard(rows, cols, primaryStage, "Travel Board", 2, 1).debugTo(new TextBoard(ROWS, COLS));
        board.init();

        GrayscaleLightBoardSurface surface = new GrayscaleLightBoardSurface(board);
        surface.init();

        ImageZone zone = new ImageZone(surface, image);
        zone.start(30);




//        startClock              (surface, COLS - CLOCK_WIDTH, 0,    CLOCK_WIDTH, ROWS);
//        startBusStopDisplay     (surface, 0, 0,                     COLS - CLOCK_WIDTH, ROWS/2);
//        startTubeStatusDisplay  (surface, 0, ROWS/2,                COLS - CLOCK_WIDTH, ROWS/2,         "bad");


    }


}
