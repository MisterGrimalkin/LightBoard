package net.amarantha.lightboard;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.stage.Stage;
import net.amarantha.lightboard.board.CLightBoard;
import net.amarantha.lightboard.board.JavaLightBoard;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.module.ApplicationModule;
import net.amarantha.lightboard.module.SimulationModule;

import static com.google.inject.util.Modules.override;
import static net.amarantha.lightboard.utility.LightBoardProperties.isSimulationMode;
import static net.amarantha.lightboard.utility.LightBoardProperties.processArgs;

public class Main extends Application {

    public static void main(String[] args) {
        processArgs(args);
        if ( isSimulationMode() ) {
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
