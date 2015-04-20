package lightboard.board.zone.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.LightBoardZone;
import lightboard.util.MessageQueue.Edge;

/**
 * Created by grimalkin on 06/04/15.
 */
public class MyTestZone extends LightBoardZone {

    public MyTestZone(LightBoardSurface surface) {
        super(surface);
    }

    @Override
    public int getContentWidth() {
        return 10;
    }

    @Override
    public int getContentHeight() {
        return 10;
    }

    private boolean fill = false;

    @Override
    public boolean render() {

        boolean drawn = false;

        drawn |= drawRect(0,0,10,10, false);
        drawn |= drawRect(2,2,6,6, false);
        drawn |= drawRect(4,4,2,2, false);

//        drawPoint(0,0);
//        drawPoint(10,0);
//        drawPoint(0,10);
//        drawPoint(10,10);

        return drawn;

    }

    private Edge[] edges = { Edge.NO_SCROLL, Edge.TOP_EDGE, Edge.RIGHT_EDGE, Edge.TOP_EDGE, Edge.BOTTOM_EDGE, Edge.LEFT_EDGE };
    private int from = 0;
    private int to = 1;

    @Override
    public   void onScrollComplete() {
        if ( ++from==edges.length ) {
            from = (int)Math.floor(Math.random()*edges.length);
        }
        if ( ++to==edges.length ) {
            to = (int)Math.floor(Math.random()*edges.length);
        }
        scroll(edges[from], edges[to]);
    }
}
