package net.amarantha.lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.board.RGBLightBoard;
import net.amarantha.lightboard.board.impl.GraphicalBoard;
import net.amarantha.lightboard.board.impl.RaspPiGlastoLightBoard;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.scene.impl.*;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.surface.RGBLightBoardSurface;
import net.amarantha.lightboard.webservice.ColourResource;
import net.amarantha.lightboard.webservice.MessageResource;
import net.amarantha.lightboard.webservice.SystemResource;
import net.amarantha.lightboard.webservice.WebService;

import static net.amarantha.lightboard.scene.SceneManager.*;
import static net.amarantha.lightboard.util.Sync.startSyncThread;

public class Main extends Application {

    private final static int COLS = 192;
    private final static int ROWS = 32;
//    private final static int COLS = 180;
//    private final static int ROWS = 16;

    @Override
    public void start(Stage primaryStage) {

        System.out.println("Starting Up....");

        WebService.startWebService(getParameters().getNamed().get("ip"));

        RGBLightBoard board;
        switch ( getBoardType() ) {
            case RASPBERRY_PI:
                RaspPiGlastoLightBoard raspberryPiLightBoard = new RaspPiGlastoLightBoard(ROWS, COLS);
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

        LightBoardSurface surface = new RGBLightBoardSurface(board);
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

        SystemResource.loadConfig();
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