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

    public TubeStatusUpdater(TextZone zone, String... lines) {
        super(zone);
        allowedLines = new ArrayList<>();
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
                    StringBuilder sb = new StringBuilder();
                    for (TubeStatus ts : tubeStatuses) {
                        if (    allowedLines.isEmpty()
                             || allowedLines.contains(ts.getLineName().toUpperCase().split(" ")[0])
                             || (allowedLines.contains("BAD") && !ts.getStatusDescription().equals("Good Service"))
                        ) {
        //                    addMessage(ts.getLineName() + ":" + ts.getStatusDescription());
                            boolean isGood = "Good Service".equals(ts.getStatusDescription());
                            String colour = (isGood ? "{green}" : "{red}" );
                            String statusDetail = (isGood ? ts.getStatusDescription()
                                    : ts.getStatusDetail()==null || ts.getStatusDetail().isEmpty()
                                            ? ts.getStatusDescription()
                                            : ts.getStatusDetail() );
                            sb.append("{yellow}").append(ts.getLineName()).append(":").append(colour).append(statusDetail).append("  ");
                        }
                    }

                    String message = sb.toString();
                    if ( message.isEmpty() ) {
                        if ( allowedLines.isEmpty() ) {
                            replaceMessage("{red}-Could Not Parse TfL Data-");
                        } else if ( allowedLines.contains("BAD") ) {
                            replaceMessage("{green}Good Service on all TfL Lines");
                        } else {
                            replaceMessage("{red}-Invalid Line Specified-");
                        }
                    } else {
                        if ( allowedLines.contains("BAD") ) {
                            message += " {yellow}Good Service All Other Lines";
                            replaceMessage(message);
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

}
