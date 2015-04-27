package lightboard.board;

import java.io.IOException;

public interface LightBoard {

    void init();

    void dump(boolean[][] data);

    int getRefreshInterval();

    int getRows();

    int getCols();

}
