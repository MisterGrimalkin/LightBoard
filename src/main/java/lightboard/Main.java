package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.RaspberryPiLightBoard;
import lightboard.board.impl.TextBoard;
import lightboard.board.surface.LightBoardSurface;
import lightboard.scene.ShopOpeningTimesScene;
import lightboard.scene.TravelInformationScene;
import lightboard.scene.WeatherForecastScene;
import lightboard.scene.WebServiceMessageScene;
import lightboard.updater.schedule.MessageResource;
import lightboard.util.ColourResource;

import static lightboard.scene.SceneManager.*;
import static lightboard.updater.WebService.startWebService;
import static lightboard.util.Sync.startSyncThread;

public class Main extends Application {

    private final static int COLS = 180;
    private final static int ROWS = 16;

    @Override
    public void start(Stage primaryStage) {

        System.out.println("Starting Up....");

        startWebService(getParameters().getNamed().get("ip"));

        LightBoard board;
        switch ( getBoardType() ) {
            case RASPBERRY_PI:
                RaspberryPiLightBoard raspberryPiLightBoard = new RaspberryPiLightBoard(ROWS, COLS);
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

        LightBoardSurface surface = new LightBoardSurface(board);
        surface.init();

        addScene(0, new WebServiceMessageScene(surface));
        addScene(1, new TravelInformationScene(surface));
        addScene(2, new ShopOpeningTimesScene(surface));
        addScene(3, new WeatherForecastScene(surface));
        startScenes();
        loadScene(1);
//        cycleScenes(90000);

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
