package net.amarantha.lightboard.module;

import com.google.inject.AbstractModule;
import javafx.stage.Stage;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.impl.GraphicalBoard;

public class SimulationModule extends AbstractModule {

    private Stage primaryStage;

    public SimulationModule(Stage primaryStage) {
        this.primaryStage = primaryStage;
        System.out.println("-- SIMULATION MODE --");
    }

    @Override
    protected void configure() {
        bind(LightBoard.class).to(GraphicalBoard.class);
        bind(Stage.class).toInstance(primaryStage);
    }

}
