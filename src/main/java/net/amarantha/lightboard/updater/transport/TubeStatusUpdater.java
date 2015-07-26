package net.amarantha.lightboard.updater.transport;

import net.amarantha.lightboard.updater.Updater;
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

public class TubeStatusUpdater extends Updater {

    private final static String TFL_TS_URL = "http://cloud.tfl.gov.uk/TrackerNet/LineStatus";

    private List<String> allowedLines;

    private TextZone markerZone;

    public TubeStatusUpdater(TextZone zone, TextZone markerZone, String... lines) {
        super(zone);
        allowedLines = new ArrayList<>();
        this.markerZone = markerZone;
        for ( String line : lines ) {
            allowedLines.add(line.toUpperCase());
        }
        System.out.println("Tube Status Updater Ready....");
    }

    @Override
    public void refresh() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                List<TubeStatus> tubeStatuses = parseDocument(queryWebService());

                clearMessages();

                if ( tubeStatuses.isEmpty() ) {
                    replaceMessage("{red}-TfL Returned No Data-");
                } else {
                    StringBuilder summarySb = new StringBuilder();
                    summarySb.append("{yellow}|");
                    int badCount = 0;
                    for (TubeStatus ts : tubeStatuses) {

                        String shortName = getShortName(ts.getLineName());
                        boolean isGood = "Good Service".equals(ts.getStatusDescription());
                        boolean isMinor = "Minor Delays".equals(ts.getStatusDescription());
                        String colour = (isGood ? "{green}" : "{red}" );
                        String statusDetail = (isGood ? ts.getStatusDescription()
                                : ts.getStatusDetail()==null || ts.getStatusDetail().isEmpty()
                                        ? ts.getStatusDescription()
                                        : ts.getStatusDetail() );
                        statusDetail.replaceAll("\n","");

                        if ( !isGood ) {
                            addMessage("{yellow}" + ts.getLineName() + ":" + colour + statusDetail);
                            badCount++;
                        }

                        if ( shortName!=null ) {
                            summarySb.append(isGood ? "{green}" : isMinor ? "{yellow}" : "{red}").append(shortName).append("{yellow}|");
                        }

                    }
                    if ( badCount==0 ) {
                        addMessage("{green}Good Service on all Tfl lines");
                    }

                    String summaryMessage = summarySb.toString().trim();
                    if ( summaryMessage.isEmpty() ) {
                        if ( allowedLines.isEmpty() ) {
                            replaceMessage("{red}-Could Not Parse TfL Data-");
                        } else if ( allowedLines.contains("BAD") ) {
                            replaceMessage("{green}Good Service on all TfL Lines");
                        } else {
                            replaceMessage("{red}-Invalid Line Specified-");
                        }
                    } else {
                        if ( allowedLines.contains("BAD") ) {
                            markerZone.addMessage(summaryMessage);
                        }
                    }

                }
            }
        }, 0);
    }

    private Document queryWebService() {
        Document doc = null;
        try {

            String xml = Http.get(TFL_TS_URL).text();
            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(stream);
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            replaceMessage("-Error Querying TfL-");
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
