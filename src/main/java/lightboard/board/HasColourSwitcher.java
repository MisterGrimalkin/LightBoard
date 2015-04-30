package lightboard.board;

public interface HasColourSwitcher {
    void red();
    void green();
    void yellow();
    void blue();
    void cycle(int ms);
}
