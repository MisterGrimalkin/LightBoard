package net.amarantha.lightboard.module;

import com.google.inject.AbstractModule;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.utility.PropertyManager;

import java.io.PrintStream;

public class ApplicationModule extends AbstractModule {

    private final int rows;
    private final int cols;

    private final Class boardClass;

    public ApplicationModule() {
        PropertyManager props = new PropertyManager();
        rows = props.getInt("boardRows", 32);
        cols = props.getInt("boardCols", 192);
        boardClass = getBoardClass(props.getString("boardClass", "net.amarantha.lightboard.board.impl.TextBoard"));
        System.out.println(cols + "x" + rows + " " + boardClass.getSimpleName());
    }

    private Class getBoardClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return TextBoard.class;
    }

    @Override
    protected void configure() {

        bindConstant().annotatedWith(Rows.class).to(rows);
        bindConstant().annotatedWith(Cols.class).to(cols);

        bind(LightBoard.class).to(boardClass);
        bind(LightBoard.class).annotatedWith(Debug.class).to(TextBoard.class);

        bind(PrintStream.class).toInstance(System.out);

    }

}
