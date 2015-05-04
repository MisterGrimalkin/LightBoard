package lightboard.font;

public class SmallFont extends Font {

    private static final boolean o = false;
    private static final boolean i = true;

    public SmallFont() {
        registerPattern('0', ZERO);
        registerPattern('1', ONE);
        registerPattern('2', TWO);
        registerPattern('3', THREE);
        registerPattern('4', FOUR);
        registerPattern('5', FIVE);
        registerPattern('6', SIX);
        registerPattern('7', SEVEN);
        registerPattern('8', EIGHT);
        registerPattern('9', NINE);
        registerPattern(':', COLON);
        registerPattern(' ', SPACE);
    }

    private final static boolean[][] ZERO =
            {{i,i,i},
            { i,o,i},
            { i,o,i},
            { i,o,i},
            { i,i,i}};

    private final static boolean[][] ONE =
            {{i},
            { i},
            { i},
            { i},
            { i}};

    private final static boolean[][] TWO =
            {{i,i,i},
            { o,o,i},
            { i,i,i},
            { i,o,o},
            { i,i,i}};

    private final static boolean[][] THREE =
            {{i,i,i},
            { o,o,i},
            { i,i,i},
            { o,o,i},
            { i,i,i}};

    private final static boolean[][] FOUR =
            {{i,o,i},
            { i,o,i},
            { i,i,i},
            { o,o,i},
            { o,o,i}};

    private final static boolean[][] FIVE =
            {{i,i,i},
            { i,o,o},
            { i,i,i},
            { o,o,i},
            { i,i,i}};

    private final static boolean[][] SIX =
            {{i,i,i},
            { i,o,o},
            { i,i,i},
            { i,o,i},
            { i,i,i}};

    private final static boolean[][] SEVEN =
            {{i,i,i},
            { o,o,i},
            { o,o,i},
            { o,o,i},
            { o,o,i}};

    private final static boolean[][] EIGHT =
            {{i,i,i},
            { i,o,i},
            { i,i,i},
            { i,o,i},
            { i,i,i}};

    private final static boolean[][] NINE =
            {{i,i,i},
            { i,o,i},
            { i,i,i},
            { o,o,i},
            { o,o,i}};

    private final static boolean[][] COLON =
            {{o,o,o},
            { o,i,o},
            { o,o,o},
            { o,i,o},
            { o,o,o}};

    private final static boolean[][] SPACE =
            {{o,o,o},
            { o,o,o},
            { o,o,o},
            { o,o,o},
            { o,o,o}};

}
