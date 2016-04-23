package net.amarantha.lightboard.updater.transport;

public class TubeStatus {

    private final String lineName;
    private final String statusDescription;
    private final String statusDetail;

    public TubeStatus(String lineName, String statusDescription, String statusDetail) {
        this.lineName = lineName;
        this.statusDescription = statusDescription;
        this.statusDetail = statusDetail;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getStatusDetail() {
        return statusDetail;
    }
}
