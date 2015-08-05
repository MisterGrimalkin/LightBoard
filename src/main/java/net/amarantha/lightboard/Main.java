package net.amarantha.lightboard;

import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.impl.CLightBoard;
import net.amarantha.lightboard.board.impl.GraphicalBoard;
import net.amarantha.lightboard.board.impl.RaspPiGlastoLightBoard;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.scene.impl.ImageScene;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.surface.CompositeSurface;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.surface.SelfTest;
import net.amarantha.lightboard.webservice.BroadcastMessageResource;
import net.amarantha.lightboard.webservice.ColourResource;
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

        SystemResource.loadConfig();

        if ( !getParameters().getUnnamed().contains("noserver") ) {
            WebService.startWebService(SystemResource.getIp());
        }

        boolean debugBoard = getParameters().getUnnamed().contains("debug");

        LightBoard board;
        switch ( getBoardType() ) {
            case RASPBERRY_PI:
                RaspPiGlastoLightBoard raspberryPiLightBoard = new RaspPiGlastoLightBoard(ROWS, COLS);
                ColourResource.addBoard(raspberryPiLightBoard);
                board = raspberryPiLightBoard;
                break;
            case C:
                CLightBoard cLightBoard = new CLightBoard();
                board = cLightBoard;
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

        LightBoardSurface surface;
        if ( debugBoard ) {
            surface = new LightBoardSurface(board, new TextBoard(ROWS, COLS));
        } else {
            surface = new LightBoardSurface(board);
        }
        surface.init();

        if ( getParameters().getUnnamed().contains("selftest") ) {
            System.out.println("Self Test...");
            new SelfTest(surface).run();
        } else {

//            addScene(0, new WebServiceMessageScene(surface));
//            addScene(1, new ImageScene(surface, "gp192x32.jpg"), null, true);
            addScene(2, new TravelInformationScene(surface), 120000, true);

            startScenes();
            cycleScenes();
            loadScene(2);

            BroadcastMessageResource.bindScene(0);
            
        }

        startSyncThread();

    }

    public enum BoardType { TEXT, GRAPHICAL, RASPBERRY_PI, C, UTILITY }

    public static void main(String[] args) {
        launch(args);
    }

    private BoardType getBoardType() {
        String boardType = getParameters().getNamed().get("board");
        if ("graphical".equals(boardType)) {
            return BoardType.GRAPHICAL;
        } else if ("utility".equals(boardType)) {
            return BoardType.UTILITY;
        } else if ( "c".equals(boardType) ) {
            return BoardType.C;
        } else if ("text".equals(boardType)) {
            return BoardType.TEXT;
        } else {
            return BoardType.RASPBERRY_PI;
        }
    }

}
