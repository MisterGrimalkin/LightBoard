package net.amarantha.lightboard.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.PropertyManager;

import java.io.PrintStream;

public class ApplicationModule extends AbstractModule {

    private final Class boardClass;

    public ApplicationModule() {
        LightBoardProperties props = new LightBoardProperties();
        boardClass = props.getBoardClass();
        System.out.println("BoardClass: " + boardClass.getSimpleName());
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
