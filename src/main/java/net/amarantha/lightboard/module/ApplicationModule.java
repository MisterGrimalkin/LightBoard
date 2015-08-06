package net.amarantha.lightboard.module;

import com.google.inject.AbstractModule;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.impl.RaspPiGlastoLightBoard;
import net.amarantha.lightboard.board.impl.TextBoard;

import java.io.PrintStream;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {

        bindConstant().annotatedWith(Cols.class).to(192);
        bindConstant().annotatedWith(Rows.class).to(32);

        bind(LightBoard.class).to(RaspPiGlastoLightBoard.class);
        bind(LightBoard.class).annotatedWith(Debug.class).to(TextBoard.class);

        bind(PrintStream.class).toInstance(System.out);

    }

}
