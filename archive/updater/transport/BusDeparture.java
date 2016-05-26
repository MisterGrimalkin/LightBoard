package net.amarantha.lightboard.updater.transport;

public class BusDeparture {

    private long stopCode;
    private String busNo;
    private String destination;
    private long offset;
    private boolean active = false;

    public BusDeparture(long stopCode, String busNo, String destination, boolean active) {
        this(stopCode, busNo, destination, active, 0);
    }

    public BusDeparture(long stopCode, String busNo, String destination, boolean active, long offset) {
        this.stopCode = stopCode;
        this.busNo = busNo;
        this.destination = destination;
        this.active = active;
        this.offset = offset;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getStopCode() {
        return stopCode;
    }

    public String getBusNo() {
        return busNo;
    }

    public String getDestination() {
        return destination;
    }

    public long getOffset() {
        return offset;
    }

    public void setStopCode(long stopCode) {
        this.stopCode = stopCode;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusDeparture busDeparture = (BusDeparture) o;

        if (stopCode != busDeparture.stopCode) return false;
        if (busNo != null ? !busNo.equals(busDeparture.busNo) : busDeparture.busNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (stopCode ^ (stopCode >>> 32));
        result = 31 * result + (busNo != null ? busNo.hashCode() : 0);
        return result;
    }

}
