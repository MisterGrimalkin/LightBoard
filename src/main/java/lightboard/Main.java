package lightboard;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.PolyLightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.RaspberryPiLightBoard2;
import lightboard.board.impl.TextBoard;
import lightboard.scene.impl.*;
import lightboard.surface.LightBoardSurface;
import lightboard.surface.PolyLightBoardSurface;
import lightboard.updater.schedule.MessageResource;
import lightboard.util.ColourResource;
import lightboard.util.SystemResource;

import static lightboard.scene.SceneManager.*;
import static lightboard.updater.WebService.startWebService;
import static lightboard.util.Sync.startSyncThread;

public class Main extends Application {

    private final static int COLS = 192;
    private final static int ROWS = 32;
//    private final static int COLS = 180;
//    private final static int ROWS = 16;

    @Override
    public void start(Stage primaryStage) {

        System.out.println("Starting Up....");

        startWebService(getParameters().getNamed().get("ip"));

        PolyLightBoard board;
        switch ( getBoardType() ) {
            case RASPBERRY_PI:
                RaspberryPiLightBoard2 raspberryPiLightBoard = new RaspberryPiLightBoard2(ROWS, COLS);
                ColourResource.addBoard(raspberryPiLightBoard);
                board = raspberryPiLightBoard;
                break;
            case GRAPHICAL:
                GraphicalBoard graphicalBoard = new GraphicalBoard(ROWS, COLS, primaryStage);
                ColourResource.addBoard(graphicalBoard);
                board = graphicalBoard;
                break;
            case TEXT:
            default:
                board = new TextBoard(ROWS, COLS);
                break;
        }
        board.init();

        LightBoardSurface surface = new PolyLightBoardSurface(board);
        surface.init();

        if ( getParameters().getUnnamed().contains("self-test") ) {
            System.out.println("Self Test...");
            surface.selfTest();
        } else {

            addScene(0, new WebServiceMessageScene(surface));
            addScene(1, new ImageScene(surface), null, true);
            addScene(2, new TravelInformationScene(surface), 20000, true);
            addScene(3, new ShowerTicketsScene(surface), 20000, false);
            addScene(4, new MessageScrollerScene(surface), 10000, false);
            startScenes();
            cycleScenes();
            loadScene(1);

            MessageResource.bindScene(0);
        }

        SystemResource.readName();
        startSyncThread();

    }

    public enum BoardType { TEXT, GRAPHICAL, RASPBERRY_PI, UTILITY }

    public static void main(String[] args) {
        launch(args);
    }

    private BoardType getBoardType() {
        String boardType = getParameters().getNamed().get("board");
        if ("graphical".equals(boardType)) {
            return BoardType.GRAPHICAL;
        } else if ("utility".equals(boardType)) {
            return BoardType.UTILITY;
        } else if ("text".equals(boardType)) {
            return BoardType.TEXT;
        } else {
            return BoardType.RASPBERRY_PI;
        }
    }

}
