package net.amarantha.lightboard.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.MockBoard;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.scene.MockBusUpdater;
import net.amarantha.lightboard.scene.MockTubeUpdater;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.updater.transport.TubeUpdater;
import net.amarantha.lightboard.utility.MockSync;
import net.amarantha.lightboard.utility.Sync;

import java.io.PrintStream;

public class ApplicationTestModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(LightBoard.class).to(MockBoard.class)
                .in(Singleton.class);
        bind(LightBoard.class).annotatedWith(Debug.class).to(TextBoard.class)
                .in(Singleton.class);

        bind(PrintStream.class).toInstance(System.out);

        bind(Sync.class).to(MockSync.class).in(Singleton.class);

        bind(BusUpdater.class).to(MockBusUpdater.class);
        bind(TubeUpdater.class).to(MockTubeUpdater.class);

    }

}
