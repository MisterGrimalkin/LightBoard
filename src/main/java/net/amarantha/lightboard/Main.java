package net.amarantha.lightboard;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.impl.GraphicalBoard;
import net.amarantha.lightboard.module.ApplicationModule;

import java.util.Arrays;
import java.util.List;

import static com.google.inject.util.Modules.override;

public class Main extends Application {

    private static boolean simulationMode = false;
    private static boolean selfTest = false;
    private static boolean suppressWebService = false;

    public static void main(String[] args) {

        List<String> params = Arrays.asList(args);
        selfTest =              params.contains("selftest");
        simulationMode =        params.contains("simulation");
        suppressWebService =    params.contains("noserver");

        if ( simulationMode ) {
            launch(args);
        } else {
            Guice.createInjector(new ApplicationModule())
                .getInstance(LightBoardApplication.class)
                    .startApplication();
        }

    }

    @Override
    public void start(Stage primaryStage) {

        Guice.createInjector(
                override(new ApplicationModule())
                    .with(new AbstractModule() {
                        @Override
                        protected void configure() {
                            bind(LightBoard.class).to(GraphicalBoard.class);
                            bind(Stage.class).toInstance(primaryStage);
                        }
                    }))
                .getInstance(LightBoardApplication.class)
                    .startApplication();

    }

}
