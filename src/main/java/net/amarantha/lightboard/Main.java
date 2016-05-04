package net.amarantha.lightboard;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.module.ApplicationModule;
import net.amarantha.lightboard.module.SimulationModule;

import java.util.Arrays;
import java.util.List;

import static com.google.inject.util.Modules.override;

public class Main extends Application {

    private static boolean simulationMode;
    private static boolean withServer;
    private static boolean testMode;

    public static void main(String[] args) {

        List<String> params = Arrays.asList(args);
        simulationMode = params.contains("simulation");
        testMode = params.contains("test");
        withServer = !params.contains("noserver");

        if ( simulationMode ) {
            launch(args);
        } else {
            Guice.createInjector(new ApplicationModule())
                .getInstance(LightBoardApplication.class)
                    .startApplication(withServer, testMode);
        }

    }

    @Override
    public void start(Stage primaryStage) {
        Guice.createInjector(
            override(new ApplicationModule())
                    .with(new SimulationModule(primaryStage))
            )
            .getInstance(LightBoardApplication.class)
                .startApplication(withServer, testMode);
    }

}
