package net.amarantha.lightboard;

import com.google.inject.Guice;
import net.amarantha.lightboard.module.ApplicationModule;

import static net.amarantha.utils.properties.PropertiesService.processArgs;


public class Main {

    public static void main(String[] args) {
        processArgs(args);
        Guice.createInjector(new ApplicationModule())
            .getInstance(LightBoardSystem.class)
                .start();
    }

    public static final String TEST_MODE = "test";
    public static final String NO_HTTP = "noserver";

}
