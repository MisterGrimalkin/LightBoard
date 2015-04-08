package lightboard.board;

/**
 * Created by grimalkin on 08/04/15.
 */
public interface GrayscaleLightBoard {

    void init();

    void dump(double[][] data);

    int getRefreshInterval();

    int getRows();

    int getCols();

}
