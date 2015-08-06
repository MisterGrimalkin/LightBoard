package net.amarantha.lightboard;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.module.ApplicationModule;
import net.amarantha.lightboard.module.SimulationModule;

import java.util.Arrays;

import static com.google.inject.util.Modules.override;

public class Main extends Application {

    public static void main(String[] args) {

        boolean simulationMode = Arrays.asList(args).contains("simulation");

        if ( simulationMode ) {
            // Launch application via JavaFX
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
                    .with(new SimulationModule(primaryStage))
            )
            .getInstance(LightBoardApplication.class)
                .startApplication();
    }

}
