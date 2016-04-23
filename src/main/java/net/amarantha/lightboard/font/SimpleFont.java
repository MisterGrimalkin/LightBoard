package net.amarantha.lightboard.font;

public class SimpleFont extends Font {

    private static final boolean o = false;
    private static final boolean i = true;

    public SimpleFont() {

        registerPattern(' ', SPACE);
        registerPattern('*', DIAMOND);

        registerPattern('\'', APOS);
        registerPattern(',', COMMA);
        registerPattern('.', FULLSTOP);
        registerPattern('!', EXCLAMATION);
        registerPattern(':', COLON);
        registerPattern(';', SEMICOLON);
        registerPattern('?', QUESTION);
        registerPattern('/', FORWARD_SLASH);
        registerPattern('\\', BACK_SLASH);

        registerPattern('<', LEFT_ARROW);
        registerPattern('>', RIGHT_ARROW);
        registerPattern('-', MINUS);
        registerPattern('=', EQUALS);
        registerPattern('+', PLUS);
        registerPattern('&', PLUS);
        registerPattern('(', OPEN_PAREN);
        registerPattern(')', CLOSE_PAREN);

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

        registerPattern('A', A);
        registerPattern('B', B);
        registerPattern('C', C);
        registerPattern('D', D);
        registerPattern('E', E);
        registerPattern('F', F);
        registerPattern('G', G);
        registerPattern('H', H);
        registerPattern('I', I);
        registerPattern('J', J);
        registerPattern('K', K);
        registerPattern('L', L);
        registerPattern('M', M);
        registerPattern('N', N);
        registerPattern('O', O);
        registerPattern('P', P);
        registerPattern('Q', Q);
        registerPattern('R', R);
        registerPattern('S', S);
        registerPattern('T', T);
        registerPattern('U', U);
        registerPattern('V', V);
        registerPattern('W', W);
        registerPattern('X', X);
        registerPattern('Y', Y);
        registerPattern('Z', Z);

        registerPattern('a', a);
        registerPattern('b', b);
        registerPattern('c', c);
        registerPattern('d', d);
        registerPattern('e', e);
        registerPattern('f', f);
        registerPattern('g', g);
        registerPattern('h', h);
        registerPattern('i', letter_i);
        registerPattern('j', j);
        registerPattern('k', k);
        registerPattern('l', l);
        registerPattern('m', m);
        registerPattern('n', n);
        registerPattern('o', letter_o);
        registerPattern('p', p);
        registerPattern('q', q);
        registerPattern('r', r);
        registerPattern('s', s);
        registerPattern('t', t);
        registerPattern('u', u);
        registerPattern('v', v);
        registerPattern('w', w);
        registerPattern('x', x);
        registerPattern('y', y);
        registerPattern('z', z);

    }

    private final static boolean[][] A =
           {{o,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {i,i,i,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {o,o,o,o}};

    private final static boolean[][] a =
           {{o,o,o,o},
            {o,o,o,o},
            {o,i,i,o},
            {o,o,o,i},
            {o,i,i,i},
            {i,o,o,i},
            {o,i,i,i},
            {o,o,o,o}};

    private final static boolean[][] B =
           {{i,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {i,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {i,i,i,o},
            {o,o,o,o}};

    private final static boolean[][] b =
           {{i,o,o,o},
            {i,o,o,o},
            {i,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,i,i,o},
            {o,o,o,o}};

    private final static boolean[][] C =
           {{o,i,i,o},
            {i,o,o,i},
            {i,o,o,o},
            {i,o,o,o},
            {i,o,o,o},
            {i,o,o,i},
            {o,i,i,o},
            {o,o,o,o}};

    private final static boolean[][] c =
           {{o,o,o,o},
            {o,o,o,o},
            {o,i,i,i},
            {i,o,o,o},
            {i,o,o,o},
            {i,o,o,o},
            {o,i,i,i},
            {o,o,o,o}};

    private final static boolean[][] D =
           {{i,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,i,i,o},
            {o,o,o,o}};

    private final static boolean[][] d =
           {{o,o,o,i},
            {o,o,o,i},
            {o,i,i,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {o,i,i,i},
            {o,o,o,o}};

    private final static boolean[][] E =
           {{i,i,i},
            {i,o,o},
            {i,o,o},
            {i,i,o},
            {i,o,o},
            {i,o,o},
            {i,i,i},
            {o,o,o}};

    private final static boolean[][] e =
            {{o,o,o,o},
            { o,o,o,o},
            { o,i,i,o},
            { i,o,o,i},
            { i,i,i,i},
            { i,o,o,o},
            { o,i,i,i},
            { o,o,o,o}};

    private final static boolean[][] F =
           {{i,i,i},
            {i,o,o},
            {i,o,o},
            {i,i,o},
            {i,o,o},
            {i,o,o},
            {i,o,o},
            {o,o,o}};

    private final static boolean[][] f =
           {{o,o,i},
            {o,i,o},
            {o,i,o},
            {i,i,i},
            {o,i,o},
            {o,i,o},
            {o,i,o},
            {o,o,o}};

    private final static boolean[][] G =
           {{o,i,i,o},
            {i,o,o,i},
            {i,o,o,o},
            {i,o,o,o},
            {i,o,i,i},
            {i,o,o,i},
            {o,i,i,o},
            {o,o,o,o}};

    private final static boolean[][] g =
           {{o,o,o,o},
            {o,o,o,o},
            {o,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {o,i,i,i},
            {o,o,o,i},
            {o,i,i,o}};

    private final static boolean[][] H =
           {{i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,i,i,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {o,o,o,o}};

    private final static boolean[][] h =
           {{i,o,o,o},
            {i,o,o,o},
            {i,i,i,o},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {i,o,o,i},
            {o,o,o,o}};

    private final static boolean[][] I =
           {{i,i,i},
            {o,i,o},
            {o,i,o},
            {o,i,o},
            {o,i,o},
            {o,i,o},
            {i,i,i},
            {o,o,o}};


    private final static boolean[][] letter_i =
           {{i},
            {o},
            {i},
            {i},
            {i},
            {i},
            {i},
            {o}};

    private final static boolean[][] J =
            {{o,i,i},
            { o,o,i},
            { o,o,i},
            { o,o,i},
            { o,o,i},
            { i,o,i},
            { o,i,o},
            { o,o,o}};

    private final static boolean[][] j =
            {{o,i},
            { o,o},
            { o,i},
            { o,i},
            { o,i},
            { o,i},
            { o,i},
            { i,o}};

    private final static boolean[][] K =
            {{i,o,o,i},
            { i,o,o,i},
            { i,o,i,o},
            { i,i,o,o},
            { i,o,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { o,o,o,o}};

    private final static boolean[][] k =
            {{i,o,o,o},
            { i,o,o,o},
            { i,o,o,i},
            { i,o,i,o},
            { i,i,o,o},
            { i,o,i,o},
            { i,o,o,i},
            { o,o,o,o}};

    private final static boolean[][] L =
            {{i,o,o},
            { i,o,o},
            { i,o,o},
            { i,o,o},
            { i,o,o},
            { i,o,o},
            { i,i,i},
            { o,o,o}};

    private final static boolean[][] l =
            {{i},
            { i},
            { i},
            { i},
            { i},
            { i},
            { i},
            { o}};

    private final static boolean[][] M =
            {{i,o,o,o,i},
            { i,i,o,i,i},
            { i,o,i,o,i},
            { i,o,i,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { o,o,o,o,o}};

    private final static boolean[][] m =
            {{o,o,o,o,o},
            { o,o,o,o,o},
            { o,i,o,i,o},
            { i,o,i,o,i},
            { i,o,i,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { o,o,o,o,o}};

    private final static boolean[][] N =
            {{i,o,o,i},
            { i,o,o,i},
            { i,i,o,i},
            { i,o,i,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,o,o,o}};

    private final static boolean[][] n =
            {{o,o,o,o},
            { o,o,o,o},
            { i,o,i,o},
            { i,i,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,o,o,o}};

    private final static boolean[][] O =
            {{o,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] letter_o =
            {{o,o,o,o},
            { o,o,o,o},
            { o,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] P =
            {{i,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,i,i,o},
            { i,o,o,o},
            { i,o,o,o},
            { i,o,o,o},
            { o,o,o,o}};

    private final static boolean[][] p =
            {{o,o,o,o},
            { o,o,o,o},
            { i,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,i,i,o},
            { i,o,o,o},
            { i,o,o,o}};

    private final static boolean[][] Q =
            {{o,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,i,o},
            { o,i,o,i},
            { o,o,o,o}};

    private final static boolean[][] q =
            {{o,o,o,o},
            { o,o,o,o},
            { o,i,i,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,i},
            { o,o,o,i},
            { o,o,o,i}};

    private final static boolean[][] R =
            {{i,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,o,o,o}};

    private final static boolean[][] r =
            {{o,o,o,o},
            { o,o,o,o},
            { i,o,i,i},
            { i,i,o,o},
            { i,o,o,o},
            { i,o,o,o},
            { i,o,o,o},
            { o,o,o,o}};

    private final static boolean[][] S =
            {{o,i,i,i},
            { i,o,o,o},
            { i,o,o,o},
            { o,i,i,o},
            { o,o,o,i},
            { o,o,o,i},
            { i,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] s =
            {{o,o,o,o},
            { o,o,o,o},
            { o,i,i,i},
            { i,o,o,o},
            { o,i,i,o},
            { o,o,o,i},
            { i,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] T =
            {{i,i,i},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,o,o}};

    private final static boolean[][] t =
            {{o,o,o},
            { o,i,o},
            { i,i,i},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,o,o}};

    private final static boolean[][] U =
            {{i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] u =
            {{o,o,o,o},
            { o,o,o,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] V =
            {{i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { o,i,o,i,o},
            { o,o,i,o,o},
            { o,o,o,o,o}};

    private final static boolean[][] v =
            {{o,o,o,o,o},
            { o,o,o,o,o},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { o,i,o,i,o},
            { o,o,i,o,o},
            { o,o,o,o,o}};

    private final static boolean[][] W =
            {{i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,i,o,i},
            { i,o,i,o,i},
            { o,i,o,i,o},
            { o,o,o,o,o}};

    private final static boolean[][] w =
            {{o,o,o,o,o},
            { o,o,o,o,o},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { i,o,i,o,i},
            { i,o,i,o,i},
            { o,i,o,i,o},
            { o,o,o,o,o}};

    private final static boolean[][] X =
            {{i,o,o,o,i},
            { i,o,o,o,i},
            { o,i,o,i,o},
            { o,o,i,o,o},
            { o,i,o,i,o},
            { i,o,o,o,i},
            { i,o,o,o,i},
            { o,o,o,o,o}};

    private final static boolean[][] x =
            {{o,o,o,o,o},
            { o,o,o,o,o},
            { i,o,o,o,i},
            { o,i,o,i,o},
            { o,o,i,o,o},
            { o,i,o,i,o},
            { i,o,o,o,i},
            { o,o,o,o,o}};

    private final static boolean[][] Y =
            {{i,o,o,o,i},
            { i,o,o,o,i},
            { o,i,o,i,o},
            { o,o,i,o,o},
            { o,o,i,o,o},
            { o,o,i,o,o},
            { o,o,i,o,o},
            { o,o,o,o,o}};

    private final static boolean[][] y =
            {{o,o,o,o},
            { o,o,o,o},
            { i,o,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,i},
            { o,o,o,i},
            { o,i,i,o}};

    private final static boolean[][] Z =
            {{i,i,i,i},
            { o,o,o,i},
            { o,o,i,o},
            { o,i,o,o},
            { i,o,o,o},
            { i,o,o,o},
            { i,i,i,i},
            { o,o,o,o}};

    private final static boolean[][] z =
            {{o,o,o,o},
            { o,o,o,o},
            { i,i,i,i},
            { o,o,i,o},
            { o,i,o,o},
            { i,o,o,o},
            { i,i,i,i},
            { o,o,o,o}};

    private final static boolean[][] SPACE =
           {{o,o,o},
            {o,o,o},
            {o,o,o},
            {o,o,o},
            {o,o,o},
            {o,o,o},
            {o,o,o},
            {o,o,o}};

    private final static boolean[][] COMMA =
           {{o,o},
            {o,o},
            {o,o},
            {o,o},
            {o,o},
            {o,o},
            {i,o},
            {i,o}};

    private final static boolean[][] FULLSTOP =
            {{o,o,o},
             {o,o,o},
             {o,o,o},
             {o,o,o},
             {o,o,o},
             {o,o,o},
             {o,i,o},
             {o,o,o}};

    private final static boolean[][] EXCLAMATION =
            {{i,o},
             {i,o},
             {i,o},
             {i,o},
             {i,o},
             {o,o},
             {i,o},
             {o,o}};

    private final static boolean[][] APOS =
            {{i},
             {i},
             {o},
             {o},
             {o},
             {o},
             {o},
             {o}};

    private final static boolean[][] QUESTION =
            {{o,i,i,i,o},
            { i,o,o,o,i},
            { o,o,o,i,o},
            { o,o,i,o,o},
            { o,o,i,o,o},
            { o,o,o,o,o},
            { o,o,i,o,o},
            { o,o,o,o,o}};

    private final static boolean[][] FORWARD_SLASH =
            {{o,o,o,i},
            { o,o,o,i},
            { o,o,i,o},
            { o,o,i,o},
            { o,i,o,o},
            { o,i,o,o},
            { i,o,o,o},
            { i,o,o,o}};

    private final static boolean[][] BACK_SLASH =
            {{i,o,o,o},
            { i,o,o,o},
            { o,i,o,o},
            { o,i,o,o},
            { o,o,i,o},
            { o,o,i,o},
            { o,o,o,i},
            { o,o,o,i}};

    private final static boolean[][] DIAMOND =
            {{o,o,o,o,o},
            { o,o,i,o,o},
            { o,i,o,i,o},
            { i,o,i,o,i},
            { o,i,o,i,o},
            { o,o,i,o,o},
            { o,o,o,o,o},
            { o,o,o,o,o}};

    private final static boolean[][] LEFT_ARROW =
            {{o,o,o,o},
            { o,o,i,o},
            { o,i,o,o},
            { i,i,i,i},
            { o,i,o,o},
            { o,o,i,o},
            { o,o,o,o},
            { o,o,o,o}};

    private final static boolean[][] RIGHT_ARROW =
            {{o,o,o,o},
            { o,i,o,o},
            { o,o,i,o},
            { i,i,i,i},
            { o,o,i,o},
            { o,i,o,o},
            { o,o,o,o},
            { o,o,o,o}};

    private final static boolean[][] ZERO =
            {{o,i,i,o},
            { i,o,o,i},
            { i,o,i,i},
            { i,i,o,i},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] ONE =
            {{o,i,o},
            { i,i,o},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { o,i,o},
            { i,i,i},
            { o,o,o}};

    private final static boolean[][] TWO =
            {{o,i,i,o},
            { i,o,o,i},
            { o,o,o,i},
            { o,o,i,o},
            { o,i,o,o},
            { i,o,o,o},
            { i,i,i,i},
            { o,o,o,o}};

    private final static boolean[][] THREE =
            {{o,i,i,o},
            { i,o,o,i},
            { o,o,o,i},
            { o,i,i,o},
            { o,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] FOUR =
            {{o,o,o,i,o},
            { o,o,i,i,o},
            { o,i,o,i,o},
            { i,o,o,i,o},
            { i,i,i,i,i},
            { o,o,o,i,o},
            { o,o,o,i,o},
            { o,o,o,o,o}};

    private final static boolean[][] FIVE =
            {{i,i,i,i},
            { i,o,o,o},
            { i,o,o,o},
            { i,i,i,o},
            { o,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] SIX =
            {{o,i,i,o},
            { i,o,o,i},
            { i,o,o,o},
            { i,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] SEVEN =
            {{i,i,i,i},
            { o,o,o,i},
            { o,o,o,i},
            { o,o,i,o},
            { o,o,i,o},
            { o,i,o,o},
            { o,i,o,o},
            { o,o,o,o}};

    private final static boolean[][] EIGHT =
            {{o,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] NINE =
            {{o,i,i,o},
            { i,o,o,i},
            { i,o,o,i},
            { o,i,i,i},
            { o,o,o,i},
            { i,o,o,i},
            { o,i,i,o},
            { o,o,o,o}};

    private final static boolean[][] EQUALS =
            {{o,o,o},
            { o,o,o},
            { i,i,i},
            { o,o,o},
            { i,i,i},
            { o,o,o},
            { o,o,o},
            { o,o,o}};

    private final static boolean[][] MINUS =
            {{o,o,o},
            { o,o,o},
            { o,o,o},
            { i,i,i},
            { o,o,o},
            { o,o,o},
            { o,o,o},
            { o,o,o}};

    private final static boolean[][] PLUS =
            {{o,o,o},
            { o,o,o},
            { o,i,o},
            { i,i,i},
            { o,i,o},
            { o,o,o},
            { o,o,o},
            { o,o,o}};

    private final static boolean[][] COLON =
            {{o,o,o},
            { o,o,o},
            { o,i,o},
            { o,o,o},
            { o,i,o},
            { o,o,o},
            { o,o,o},
            { o,o,o}};

    private final static boolean[][] SEMICOLON =
            {{o,o,o},
            { o,i,o},
            { o,o,o},
            { o,o,o},
            { o,o,o},
            { o,i,o},
            { o,i,o},
            { o,o,o}};

    private final static boolean[][] OPEN_PAREN =
            {{o,o,i},
            { o,i,o},
            { i,o,o},
            { i,o,o},
            { i,o,o},
            { o,i,o},
            { o,o,i},
            { o,o,o}};

    private final static boolean[][] CLOSE_PAREN =
            {{i,o,o},
            { o,i,o},
            { o,o,i},
            { o,o,i},
            { o,o,i},
            { o,i,o},
            { i,o,o},
            { o,o,o}};

}
