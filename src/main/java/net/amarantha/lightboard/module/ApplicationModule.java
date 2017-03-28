package net.amarantha.lightboard.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.TextBoard;
import net.amarantha.utils.properties.PropertiesService;
import net.amarantha.utils.properties.PropertyNotFoundException;

import java.io.PrintStream;

public class ApplicationModule extends AbstractModule {

    private Class boardClass;

    public ApplicationModule() {
        PropertiesService props = new PropertiesService();
        try {
            boardClass = props.getClass("boardClass");
            System.out.println("BoardClass: " + boardClass.getSimpleName());
        } catch (PropertyNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    protected void configure() {

        bind(LightBoard.class).to(boardClass)
                .in(Singleton.class);
        bind(LightBoard.class).annotatedWith(Debug.class).to(TextBoard.class)
                .in(Singleton.class);

        bind(PrintStream.class).toInstance(System.out);

    }

}
