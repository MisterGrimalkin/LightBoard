package lightboard.updater;

import lightboard.zone.impl.TextZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.javalite.http.Http;

import java.util.*;


public class ShowerTicketUpdater extends Updater {

    private TextZone name1;
    private TextZone name2;
    private TextZone tickets1;
    private TextZone tickets2;

    public ShowerTicketUpdater(TextZone name1, TextZone name2, TextZone tickets1, TextZone tickets2) {
        super(name1);
        this.name1 = name1;
        this.name2 = name2;
        this.tickets1 = tickets1;
        this.tickets2 = tickets2;
        name1.addMessage(id, "{green}Female");
        name2.addMessage(id, "{green}Male");
    }

    @Override
    public void refresh() {

        try {

            String response = Http.get("http://192.168.0.17:8002/shower/next-tickets").text();

            JSONObject responseObj = JSONObject.fromObject(response);

            Map<String, List<Integer>> tickets = new HashMap<>();

            JSONArray blockArray = (JSONArray) responseObj.get("blocks");
            Iterator<JSONObject> blockIter = blockArray.iterator();
            while (blockIter.hasNext()) {

                JSONObject block = blockIter.next();
                String name = block.getString("name");

                List<Integer> ticketNumbers = new ArrayList<>();

                JSONArray ticketArray = (JSONArray) block.get("tickets");
                Iterator<JSONObject> innerIter = ticketArray.iterator();
                while (innerIter.hasNext()) {
                    JSONObject ticket = innerIter.next();
                    Integer number = ticket.getInt("number");
                    ticketNumbers.add(number);
                }
                Collections.sort(ticketNumbers);
                tickets.put(name, ticketNumbers);

            }

            List<Integer> femaleTickets = tickets.get("female");
            String msg = "";
            for (Integer t : femaleTickets) {
                msg += t + "  ";
            }
            tickets1.clearMessages(id);
            tickets1.addMessage(id, msg);

            List<Integer> maleTickets = tickets.get("male");
            msg = "";
            for (Integer t : maleTickets) {
                msg += t + "  ";
            }
            tickets2.clearMessages(id);
            tickets2.addMessage(id, msg);

        } catch ( Exception e ) {
            System.err.println("Error getting shower tickets: " + e.getMessage());
            tickets1.clearMessages(id);
            tickets1.addMessage(id, "{red}Error Getting Tickets");
            tickets2.clearMessages(id);
            tickets2.addMessage(id, "{red}Error Getting Tickets");
        }

    }
}
