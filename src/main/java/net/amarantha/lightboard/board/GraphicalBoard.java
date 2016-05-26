package net.amarantha.lightboard.board;

import com.google.inject.Inject;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.amarantha.lightboard.module.Debug;
import net.amarantha.lightboard.utility.Sync;

/**
 * UI Simulation of a colour LightBoard
 */
public class GraphicalBoard implements LightBoard {

    private final static Long LED_REFRESH_TIME = 60L;

    private final static String BLACK_BACKGROUND = "-fx-background-color: black;";

    private int rows;
    private int cols;

    private Circle[][] leds;

    private final Sync sync;

    // Background colour
    private double backR = 0.05;
    private double backG = 0.05;
    private double backB = 0.05;

    // Simulate a board which only has Red and Green LEDs (does not apply to colour override)
    private final static boolean RG_ONLY = true;

    private Stage stage;
    private String title;
    private int ledRadius;
    private int d;
    private int spacer;

    @Inject
    public GraphicalBoard(Sync sync, Stage stage) {
        this(sync, stage, "LightBoard Simulation", 2, 0);
    }

    public GraphicalBoard(Sync sync, Stage stage, String title, int ledRadius, int spacer) {
        this.sync = sync;
        this.stage = stage;
        this.title = title;
        this.ledRadius = ledRadius;
        this.spacer = spacer;
    }

    @Override
    public void init(int rows, int cols) {

        System.out.println("Starting UI Simulation LightBoard....");

        this.rows = rows;
        this.cols = cols;

        leds = new Circle[rows][cols];
        d = ledRadius * 2;

        // Build UI components
        final Pane pane = new Pane();
        pane.setStyle(BLACK_BACKGROUND);
        addMouseHandlers(pane);
        Group board = new Group();
        pane.getChildren().add(board);

        // Create LED Board
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Circle led = new Circle(ledRadius - spacer, Color.color(backR, backG, backB));
                led.setCenterX(d + col * d);
                led.setCenterY(d + row * d);
                board.getChildren().add(led);
                leds[row][col] = led;
            }
        }

        // Start UI
        stage.setScene(new Scene(pane, getWidthPixels(), getHeightPixels()));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle(title);
        stage.show();

        // Shut down application when window is closed
        stage.setOnCloseRequest(event -> {
            sync.stopSyncThread();
            System.exit(0);
        });

        System.out.println("Board Ready");

    }


    ///////////
    // Mouse //
    ///////////

    private void addMouseHandlers(final Pane pane) {

        // Drag window by holding button down anywhere inside it
        pane.setOnMouseDragged((e) -> {
            if (dragOffsetX == null || dragOffsetY == null) {
                dragOffsetX = e.getX();
                dragOffsetY = e.getY();
            }
            stage.setX(stage.getX() + e.getX() - dragOffsetX);
            stage.setY(stage.getY() + e.getY() - dragOffsetY);
        });
        pane.setOnMouseReleased((e) -> {
            dragOffsetX = null;
            dragOffsetY = null;
        });

        // CTRL+SHIFT+MOUSE-CLICK => update board state to console
        pane.setOnMouseClicked((e) -> {
            if (e.isControlDown() && e.isShiftDown()) {
                dumpToDebug = !dumpToDebug;
            }
        });

        // RIGHT-CLICK => reset UI window (because it messes up all the time - bug in JaxaFX maybe???)
        pane.setOnMouseClicked((e) -> {
            if ( e.getButton()==MouseButton.SECONDARY ) {
                stage.hide();
                stage = new Stage();
                init(rows, cols);
            } else {
                if (e.isControlDown() && e.isAltDown()) {
                    if (e.isShiftDown()) {
                        stage.hide();
                        stage = new Stage();
                        init(rows, cols);
                    } else {
                        dumpToDebug = true;
                    }
                }
            }
        });

    }

    private Double dragOffsetX = null;
    private Double dragOffsetY = null;


    ////////////////
    // LightBoard //
    ////////////////

    @Override
    public void update(double[][][] data) {
        if ( dumpToDebug ) {
            debugBoard.update(data);
            dumpToDebug = false;
        }
        for (int r = 0; r < data[0].length; r++) {
            for (int c = 0; c < data[0][0].length; c++) {
                double red = data[0][r][c];
                double green = data[1][r][c];
                double blue = data[2][r][c];
                if (leds[r][c] != null) {
                    leds[r][c].setFill(Color.color(red, green, blue));
                }
            }
        }
    }


    /////////////////////////
    // Board Specification //
    /////////////////////////

    @Override
    public Long getUpdateInterval() {
        return LED_REFRESH_TIME;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public void sleep() {

    }

    @Override
    public void wake() {

    }

    public int getHeightPixels() {
        return (rows + 1) * d;
    }

    public int getWidthPixels() {
        return (cols + 1) * d;
    }

    ///////////////
    // Debugging //
    ///////////////

    private boolean dumpToDebug = false;
    @Inject @Debug private LightBoard debugBoard;

}