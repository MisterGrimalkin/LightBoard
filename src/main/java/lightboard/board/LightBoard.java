package lightboard.board;

public interface LightBoard {

    void init();

    void dump(boolean[][] data);

    Long getRefreshInterval();

    int getRows();

    int getCols();

}
