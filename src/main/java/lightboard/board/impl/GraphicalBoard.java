package lightboard.board.impl;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lightboard.board.LightBoard;
import lightboard.board.MonochromeLightBoard;
import lightboard.board.PolychromeLightBoard;

public class GraphicalBoard implements MonochromeLightBoard, PolychromeLightBoard {

    private final static String BLACK_BACKGROUND = "-fx-background-color: black;";

    private final static double RED_MIN = 0.15;
    private final static double RED_MAX = 1.0;

    private final static double GREEN_MIN = 0.0;
    private final static double GREEN_MAX = 0.0;

    private final static double BLUE_MIN = 0.0;
    private final static double BLUE_MAX = 0.0;

    private final static Color ON = Color.color(  RED_MAX, GREEN_MAX, BLUE_MAX );
    private final static Color OFF = Color.color( RED_MIN, GREEN_MIN, BLUE_MIN);

    private final static double FULL_OPACITY = 1.0;
    private final static double FADED_OPACITY = 0.8;

    private final static int LED_REFRESH_TIME = 80;

    private final int rows;
    private final int cols;

    private Circle[][] leds;

    private int d;

    private final Stage stage;

    private String title;
    private int ledRadius;
    private int spacer;


    public GraphicalBoard(int rows, int cols, Stage stage, String title) {
        this(rows, cols, stage, title, 3, 1);
    }

    public GraphicalBoard(int rows, int cols, Stage stage, String title, int ledRadius) {
        this(rows, cols, stage, title, ledRadius, 1);
    }

    public GraphicalBoard(int rows, int cols, Stage stage, String title, int ledRadius, int spacer) {
        this.rows = rows;
        this.cols = cols;
        this.stage = stage;
        this.title = title;
        this.ledRadius = ledRadius;
        this.spacer = spacer;
    }

    @Override
    public void init() {

        leds = new Circle[rows][cols];
        d = ledRadius*2;

        // Build UI components
        final Pane pane = new Pane();
        pane.setStyle(BLACK_BACKGROUND);
        addMouseHandlers(stage, pane);
        Group board = new Group();
        pane.getChildren().add(board);

        // Create LED Board
        for ( int row=0; row<rows; row++ ) {
            for ( int col=0; col<cols; col++ ) {
                Circle led = new Circle(ledRadius - spacer, OFF);
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
        stage.setOpacity(FADED_OPACITY);
        stage.setTitle(title);
        stage.show();

    }

    private void addMouseHandlers(final Stage stage, Pane pane) {

        pane.setOnMouseEntered(event -> stage.setOpacity(FULL_OPACITY));
        pane.setOnMouseExited(event -> stage.setOpacity(FADED_OPACITY));

        pane.setOnMouseDragged((e)->{
            if ( dragOffsetX==null || dragOffsetY==null ) {
                dragOffsetX = e.getX();
                dragOffsetY = e.getY();
            }
            stage.setX(stage.getX()+e.getX()-dragOffsetX);
            stage.setY(stage.getY()+e.getY()-dragOffsetY);
        });

        pane.setOnMouseReleased((e) -> {
            dragOffsetX = null;
            dragOffsetY = null;
        });

        pane.setOnMouseClicked((e) -> {
           if ( e.isControlDown() && e.isShiftDown() ) {
               dumpUiState();
               dumpToDebug = !dumpToDebug;
           }
        });

    }


    ///////////////////////////
    // Polychrome LightBoard //
    ///////////////////////////

    private Double dragOffsetX = null;
    private Double dragOffsetY = null;

    @Override
    public void dump(double[][][] data) {
        for ( int r=0; r<data.length; r++ ) {
            dumpPolyRow(r, data[r]);
        }
    }

    private void dumpPolyRow(int rowNumber, double[][] data) {
        Circle[] rowLights = leds[rowNumber];
        for ( int c=0; c<rowLights.length; c++ ) {
            double red = data[c][0];
            double green = data[c][1];
            double blue = data[c][2];
            rowLights[c].setFill(Color.color(red,green,blue));
        }
    }




    ///////////////////////////
    // Monochrome LightBoard //
    ///////////////////////////

    @Override
    public void dump(double[][] data) {
        for ( int r=0; r<data.length; r++ ) {
            dumpMonoRow(r, data[r]);
        }
    }

    private void dumpMonoRow(int rowNumber, double... data) {
        Circle[] rowLights = leds[rowNumber];
        for ( int c=0; c<rowLights.length; c++ ) {
            double red = RED_MIN + (data[c]*(RED_MAX-RED_MIN));
            double green = GREEN_MIN + (data[c]*(GREEN_MAX-GREEN_MIN));
            double blue = BLUE_MIN + (data[c]*(BLUE_MAX-BLUE_MIN));
            rowLights[c].setFill(Color.color(red,green,blue));
        }
    }


    ///////////////////////
    // Binary LightBoard //
    ///////////////////////

    @Override
    public synchronized void dump(boolean[][] data) {
        if ( dumpToDebug && debugBoard!=null) {
            debugBoard.dump(data);
            dumpToDebug = false;
        }
        for ( int r=0; r<data.length; r++ ) {
            dumpBinaryRow(r, data[r]);
        }
    }

    private void dumpBinaryRow(int rowNumber, boolean... rowData) {
        Circle[] rowLights = leds[rowNumber];
        for ( int c=0; c<rowLights.length; c++ ) {
            double red = rowData[c] ? RED_MAX : RED_MIN;
            double green = rowData[c] ? GREEN_MAX : GREEN_MIN;
            double blue = rowData[c] ? BLUE_MAX : BLUE_MIN;
            rowLights[c].setFill(Color.color(red,green,blue));
        }
    }


    /////////////////////////
    // Board Specification //
    /////////////////////////

    @Override
    public int getRefreshInterval() {
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

    public int getHeightPixels() {
        return  (rows+1) * d;
    }

    public int getWidthPixels() {
        return (cols+1) * d;
    }


    ///////////////
    // Debugging //
    ///////////////

    private void dumpUiState() {
        if ( debugBoard!=null ) {
            boolean[][] state = new boolean[rows][cols];
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    state[row][col] = (leds[row][col].getFill().equals(ON));
                    leds[row][col].setFill(Color.color(  0.0, 0.0, 0.8));
                }
            }
            debugBoard.dump(state);
        }
    }

    private boolean dumpToDebug = false;
    private LightBoard debugBoard;
    public GraphicalBoard debugTo(LightBoard debugBoard) {
        this.debugBoard = debugBoard;
        return this;
    }

}
