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

    public static void main(String[] args) {

        List<String> params = Arrays.asList(args);
        simulationMode = params.contains("simulation");
        withServer = !params.contains("noserver");

        if ( simulationMode ) {
            // Launch application via JavaFX
            launch(args);
        } else {
            Guice.createInjector(new ApplicationModule())
                .getInstance(LightBoardApplication.class)
                    .startApplication(withServer);
        }

    }

    @Override
    public void start(Stage primaryStage) {
        Guice.createInjector(
            override(new ApplicationModule())
                    .with(new SimulationModule(primaryStage))
            )
            .getInstance(LightBoardApplication.class)
                .startApplication(withServer);
    }

}
