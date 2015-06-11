package net.amarantha.lightboard.entity;

import static net.amarantha.lightboard.entity.Edge.NO_SCROLL;
import static net.amarantha.lightboard.entity.HPosition.CENTRE;
import static net.amarantha.lightboard.entity.VPosition.MIDDLE;

public class MessageWrapper {

    private String message;

    private Edge scrollFrom;
    private Edge scrollTo;

    private HPosition hPosition;
    private VPosition vPosition;

    int restDuration;


    public MessageWrapper(String message) {
        this(message, NO_SCROLL, NO_SCROLL, CENTRE, MIDDLE, 3000);
    }

    public MessageWrapper(String message, Edge scrollFrom, Edge scrollTo, HPosition hPosition, VPosition vPosition, int restDuration) {
        this.message = message;
        this.scrollFrom = scrollFrom;
        this.scrollTo = scrollTo;
        this.hPosition = hPosition;
        this.vPosition = vPosition;
        this.restDuration = restDuration;
    }

    public String getMessage() {
        return message;
    }

    public Edge getScrollFrom() {
        return scrollFrom;
    }

    public Edge getScrollTo() {
        return scrollTo;
    }

    public HPosition getHPosition() {
        return hPosition;
    }

    public VPosition getVPosition() {
        return vPosition;
    }

    public int getRestDuration() {
        return restDuration;
    }

}
