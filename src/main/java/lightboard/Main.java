package lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import lightboard.board.LightBoard;
import lightboard.board.impl.GraphicalBoard;
import lightboard.board.impl.RaspberryPiLightBoard;
import lightboard.board.impl.TextBoard;
import lightboard.board.surface.LightBoardSurface;
import lightboard.scene.TravelInformationScene;
import lightboard.scene.WebServiceMessageScene;
import lightboard.updater.schedule.MessageResource;

import static lightboard.scene.SceneManager.addScene;
import static lightboard.scene.SceneManager.cycleScenes;
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

        addScene(0, new TravelInformationScene(surface));
        addScene(1, new WebServiceMessageScene(surface));
        cycleScenes(90000);

        MessageResource.bindScene(1);

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
