package net.amarantha.lightboard;

import com.google.inject.Guice;
import net.amarantha.lightboard.module.ApplicationModule;

import static net.amarantha.lightboard.utility.LightBoardProperties.processArgs;

public class Main {

    public static void main(String[] args) {
        processArgs(args);
        Guice.createInjector(new ApplicationModule())
            .getInstance(LightBoardApplication.class)
                .startApplication();
    }

}
