package lightboard.board;

public interface LightBoard {

    void init();

    void dump(boolean[][] data);

    int getRefreshInterval();

    int getRows();

    int getCols();

}
