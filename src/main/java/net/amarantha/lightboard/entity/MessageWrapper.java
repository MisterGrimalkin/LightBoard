package net.amarantha.lightboard.entity;

import static net.amarantha.lightboard.entity.AlignH.CENTRE;
import static net.amarantha.lightboard.entity.AlignV.MIDDLE;
import static net.amarantha.lightboard.entity.Edge.NO_SCROLL;

public class MessageWrapper {

    private String message;
    private Edge scrollFrom;
    private Edge scrollTo;
    private AlignH alignH;
    private AlignV alignV;
    int restDuration;

    public MessageWrapper(String message) {
        this(message, NO_SCROLL, NO_SCROLL, CENTRE, MIDDLE, 3000);
    }

    public MessageWrapper(String message, Edge scrollFrom, Edge scrollTo, AlignH alignH, AlignV alignV, int restDuration) {
        this.message = message;
        this.scrollFrom = scrollFrom;
        this.scrollTo = scrollTo;
        this.alignH = alignH;
        this.alignV = alignV;
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

    public AlignH setAlignH() {
        return alignH;
    }

    public AlignV setAlignV() {
        return alignV;
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
        if (alignH != that.alignH) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (scrollFrom != that.scrollFrom) return false;
        if (scrollTo != that.scrollTo) return false;
        if (alignV != that.alignV) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (scrollFrom != null ? scrollFrom.hashCode() : 0);
        result = 31 * result + (scrollTo != null ? scrollTo.hashCode() : 0);
        result = 31 * result + (alignH != null ? alignH.hashCode() : 0);
        result = 31 * result + (alignV != null ? alignV.hashCode() : 0);
        result = 31 * result + restDuration;
        return result;
    }

}
