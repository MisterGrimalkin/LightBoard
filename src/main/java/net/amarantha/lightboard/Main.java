package net.amarantha.lightboard;

import com.google.inject.Guice;
import net.amarantha.lightboard.module.ApplicationModule;

import static net.amarantha.lightboard.utility.LightBoardProperties.isSimulationMode;
import static net.amarantha.lightboard.utility.LightBoardProperties.processArgs;

public class Main { //extends Application {

    public static void main(String[] args) {
        processArgs(args);
        if ( isSimulationMode() ) {
            System.out.println("Simulation Mode is not Currently Supported");
//            launch(args);
        } else {
            Guice.createInjector(new ApplicationModule())
                .getInstance(LightBoardApplication.class)
                    .startApplication();
        }
    }

//    @Override
//    public void start(Stage primaryStage) {
//        Guice.createInjector(
//            override(new ApplicationModule())
//                .with(new SimulationModule(primaryStage))
//            )
//            .getInstance(LightBoardApplication.class)
//                .startApplication();
//    }

}
