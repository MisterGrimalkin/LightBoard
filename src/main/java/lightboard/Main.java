package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.PolyLightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.TextBoard;
import lightboard.scene.impl.ImageScene;
import lightboard.scene.impl.ShopOpeningTimesScene;
import lightboard.scene.impl.TravelInformationScene;
import lightboard.scene.impl.WebServiceMessageScene;
import lightboard.surface.LightBoardSurface;
import lightboard.surface.PolyLightBoardSurface;
import lightboard.updater.schedule.MessageResource;
import lightboard.util.ColourResource;

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
//            case RASPBERRY_PI:
//                RaspberryPiLightBoard raspberryPiLightBoard = new RaspberryPiLightBoard(ROWS, COLS);
//                ColourResource.addBoard(raspberryPiLightBoard);
//                board = raspberryPiLightBoard;
//                break;
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

        addScene(0, new WebServiceMessageScene(surface));
        addScene(1, new TravelInformationScene(surface), 10000, true);
        addScene(2, new ShopOpeningTimesScene(surface), null, false);
        addScene(3, new ImageScene(surface), null, true);
//        addScene(3, new WeatherForecastScene(surface));
        startScenes();
        cycleScenes(10000);

        MessageResource.bindScene(0);

        startSyncThread();

    }

    public enum BoardType { TEXT, GRAPHICAL, RASPBERRY_PI }

    public static void main(String[] args) {
        launch(args);
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

}
