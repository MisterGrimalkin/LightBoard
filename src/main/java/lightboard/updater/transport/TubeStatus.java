package lightboard.updater.transport;

public class TubeStatus {

    private final String lineName;
    private final String statusDescription;

    public TubeStatus(String lineName, String statusDescription) {
        this.lineName = lineName;
        this.statusDescription = statusDescription;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

}
