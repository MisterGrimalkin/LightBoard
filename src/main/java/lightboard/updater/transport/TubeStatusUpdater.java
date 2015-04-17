package lightboard.updater.transport;

import lightboard.board.zone.impl.TextZone;
import lightboard.updater.Updater;
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

public class TubeStatusUpdater extends Updater {

    private final static String TFL_TS_URL = "http://cloud.tfl.gov.uk/TrackerNet/LineStatus";

    private List<String> allowedLines;

    public TubeStatusUpdater(TextZone zone, String... lines) {
        super(zone);
        allowedLines = new ArrayList<>();
        for ( String line : lines ) {
            allowedLines.add(line.toUpperCase());
        }
    }

    @Override
    public void refresh() {

        List<TubeStatus> tubeStatuses = parseDocument(queryWebService());
        if ( tubeStatuses.isEmpty() ) {
            replaceMessage("-TFL Returned No Data-");
        } else {

            StringBuilder sb = new StringBuilder();
            for (TubeStatus ts : tubeStatuses) {
                if (    allowedLines.isEmpty()
                     || allowedLines.contains(ts.getLineName().toUpperCase().split(" ")[0])
                     || (allowedLines.contains("BAD") && !ts.getStatusDescription().equals("Good Service"))
                ) {
                    sb.append(ts.getLineName()).append(":").append(ts.getStatusDescription()).append("  ");
                }
            }

            String message = sb.toString();
            if ( message.isEmpty() ) {
                if ( allowedLines.isEmpty() ) {
                    message = "-Could Not Parse TFL Data-";
                } else if ( allowedLines.contains("BAD") ) {
                    message = "All Tubes Good Service";
                } else {
                    message = "-Invalid Line Specified-";
                }
            } else {
                if ( allowedLines.contains("BAD") ) {
                    message += "   Good Service All Other Lines";
                }
            }
            System.out.println(message);
            replaceMessage(message);
        }

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
            replaceMessage("-Error Querying TFL-");
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

                NodeList lines = lineStatus.getElementsByTagName("Line");
                String name = ((Element) lines.item(0)).getAttribute("Name");

                NodeList statuses = lineStatus.getElementsByTagName("Status");
                String description = ((Element) statuses.item(0)).getAttribute("Description");

                if (name != null && description != null) {
                    result.add(new TubeStatus(name, description));
                }

            }
        }
        return result;
    }

}
