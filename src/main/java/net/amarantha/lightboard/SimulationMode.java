package net.amarantha.lightboard;

import com.google.inject.Guice;
import com.google.inject.util.Modules;
import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.module.ApplicationModule;
import net.amarantha.lightboard.module.SimulationModule;

import static net.amarantha.utils.properties.PropertiesService.processArgs;


public class SimulationMode extends Application {

    public static void main(String[] args) {
        processArgs(args);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Guice.createInjector(
            Modules.override(new ApplicationModule())
                .with(new SimulationModule(primaryStage)))
                    .getInstance(LightBoardSystem.class)
                        .start();
    }

}
