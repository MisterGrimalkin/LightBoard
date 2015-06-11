package lightboard.util;

import lightboard.board.ColourSwitcher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("colour")
public class ColourResource {

    private static List<ColourSwitcher> boards = new ArrayList<>();

    public static <T extends ColourSwitcher> void addBoard(T board) {
        boards.add(board);
    }

    @GET
    public Response getColours() {
        JSONObject jsonWrapper = new JSONObject();
        JSONArray ja = new JSONArray();
        Set<String> colourNames = new HashSet<>();
        for ( ColourSwitcher board : boards ) {
            for ( String c : board.getSupportedColours() ) {
                colourNames.add(c);
            }
        }
        for ( String name : colourNames ) {
                JSONObject jsonColour = new JSONObject();
                jsonColour.put("name", name);
                ja.add(jsonColour);
        }
        jsonWrapper.put("colours", ja);
        String result = jsonWrapper.toString();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(result)
                .build();
    }

    @POST
    public Response colour(@QueryParam("name") final String name) {
        for (ColourSwitcher colourSwitcher : boards ) {
            colourSwitcher.setColour(name);
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Colour Changed")
                .build();
    }

}
