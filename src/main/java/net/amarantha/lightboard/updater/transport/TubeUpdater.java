package net.amarantha.lightboard.updater.transport;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.impl.TextZone;
import org.javalite.http.Http;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TubeUpdater extends Updater {

    private final static String TFL_TS_URL = "http://cloud.tfl.gov.uk/TrackerNet/LineStatus";

    private TextZone detailZone;
    private TextZone summaryZone;

    @Inject private PropertyManager props;

    @Inject
    public TubeUpdater(Sync sync) {
        super(sync);
    }

    public TubeUpdater setZones(TextZone detailZone, TextZone summaryZone) {
        this.detailZone = detailZone;
        this.summaryZone = summaryZone;
        return this;
    }

    @Override
    public void refresh() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                List<TubeStatus> tubeStatuses = parseDocument(queryWebService());

                detailZone.clearMessages();
                if ( summaryZone!=null ) {
                    summaryZone.clearMessages();
                }

                if ( tubeStatuses.isEmpty() ) {
                    detailZone.replaceMessage("{red}TfL returned no data");
                    if ( summaryZone!=null ) {
                        summaryZone.replaceMessage("{red}Error Connecting to TfL");
                    }
                } else {
                    StringBuilder summarySb = new StringBuilder();
                    summarySb.append("{yellow}|");
                    int badCount = 0;
                    for (TubeStatus ts : tubeStatuses) {

                        String shortName = getShortName(ts.getLineName());
                        boolean isGood = "Good Service".equalsIgnoreCase(ts.getStatusDescription());
                        boolean isMinor = "Minor Delays".equalsIgnoreCase(ts.getStatusDescription());
                        String colour = (isGood ? "{green}" : isMinor ? "{yellow}" : "{red}" );
                        String statusDetail = (isGood ? ts.getStatusDescription()
                                : ts.getStatusDetail()==null || ts.getStatusDetail().isEmpty()
                                        ? ts.getStatusDescription()
                                        : ts.getStatusDetail() );

                        String[] lines = statusDetail.split("\n");
                        if ( lines.length>1 ) {
                            statusDetail = "";
                            for ( int i=0; i<lines.length; i++ ) {
                                statusDetail += lines[i] + " ";
                            }
                        }

                        if ( !isGood ) {
                            detailZone.addMessage("{yellow}" + ts.getLineName() + ":" + colour + statusDetail);
                            badCount++;
                        }

                        if ( shortName!=null ) {
                            summarySb.append(colour).append(shortName).append("{yellow}|");
                        }

                    }

                    if ( badCount==0 ) {
                        if ( props.getString("showTubeSummary", "true").equals("true") ) {
                            detailZone.addMessage("   ");
                        } else {
                            detailZone.addMessage("{green}Good service on all TfL lines");
                        }
                    }

                    if ( summaryZone!=null ) {
                        String summaryMessage = summarySb.toString().trim();
                        summaryZone.clearMessages();
                        summaryZone.addMessage(summaryMessage);
                    }

                }
            }
        }, 0);
    }

    protected String doTflCall() {
        return Http.get(TFL_TS_URL).text();
    }

    private Document queryWebService() {
        Document doc = null;
        try {
            String xml = doTflCall();
            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(stream);
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            summaryZone.replaceMessage("-Error Querying TfL-");
            e.printStackTrace();
        }
        return doc;
    }

    private List<TubeStatus> parseDocument(Document doc) {
        List<TubeStatus> result = new ArrayList<>();

        if ( doc!=null ) {
            NodeList lineStatuses = doc.getElementsByTagName("LineStatus");

            for (int ls = 0; ls < lineStatuses.getLength(); ls++) {

                Element lineStatus = (Element) lineStatuses.item(ls);
                String detail = lineStatus.getAttribute("StatusDetails");

                NodeList lines = lineStatus.getElementsByTagName("Line");
                String name = ((Element) lines.item(0)).getAttribute("Name");

                NodeList statuses = lineStatus.getElementsByTagName("Status");
                String description = ((Element) statuses.item(0)).getAttribute("Description");

                if (name != null && description != null) {
                    result.add(new TubeStatus(name, description, detail));
                }

            }
        }
        return result;
    }

    private String getShortName(String tubeLineName) {
        if ( "Bakerloo".equalsIgnoreCase(tubeLineName) ) {
            return "BAK";
        }
        if ( "Central".equalsIgnoreCase(tubeLineName) ) {
            return "CEN";
        }
        if ( "Circle".equalsIgnoreCase(tubeLineName) ) {
            return "CIR";
        }
        if ( "District".equalsIgnoreCase(tubeLineName) ) {
            return "DIS";
        }
        if ( "Hammersmith and City".equalsIgnoreCase(tubeLineName) ) {
            return "HAM";
        }
        if ( "Jubilee".equalsIgnoreCase(tubeLineName) ) {
            return "JUB";
        }
        if ( "Metropolitan".equalsIgnoreCase(tubeLineName) ) {
            return "MET";
        }
        if ( "Northern".equalsIgnoreCase(tubeLineName) ) {
            return "NOR";
        }
        if ( "Piccadilly".equalsIgnoreCase(tubeLineName) ) {
            return "PIC";
        }
        if ( "Victoria".equalsIgnoreCase(tubeLineName) ) {
            return "VIC";
        }
        if ( "Waterloo and City".equalsIgnoreCase(tubeLineName) ) {
            return "WAT";
        }
        if ( "DLR".equalsIgnoreCase(tubeLineName) ) {
            return "DLR";
        }
        if ( "Overground".equalsIgnoreCase(tubeLineName) ) {
            return "OVR";
        }
        return null;
    }

}
