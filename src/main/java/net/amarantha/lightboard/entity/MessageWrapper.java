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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageWrapper that = (MessageWrapper) o;

        if (restDuration != that.restDuration) return false;
        if (hPosition != that.hPosition) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (scrollFrom != that.scrollFrom) return false;
        if (scrollTo != that.scrollTo) return false;
        if (vPosition != that.vPosition) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (scrollFrom != null ? scrollFrom.hashCode() : 0);
        result = 31 * result + (scrollTo != null ? scrollTo.hashCode() : 0);
        result = 31 * result + (hPosition != null ? hPosition.hashCode() : 0);
        result = 31 * result + (vPosition != null ? vPosition.hashCode() : 0);
        result = 31 * result + restDuration;
        return result;
    }
}
