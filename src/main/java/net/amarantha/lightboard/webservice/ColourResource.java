package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.ColourSwitcher;
import net.amarantha.lightboard.board.LightBoard;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Path("colour")
public class ColourResource extends Resource {

    private static ColourSwitcher board;

    public ColourResource() {}

    @Inject
    public ColourResource(LightBoard lightBoard) {
        if ( lightBoard instanceof ColourSwitcher ) {
            board = (ColourSwitcher)lightBoard;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColours() {
        if ( board!=null ) {
            JSONObject json = new JSONObject();
            JSONArray ja = new JSONArray();


            Set<String> colourNames = new HashSet<>();
            colourNames.addAll(board.getSupportedColours());

            for (String name : colourNames) {
                JSONObject jsonColour = new JSONObject();
                jsonColour.put("name", name);
                ja.add(jsonColour);
            }
            json.put("colours", ja);

            return ok(json.toString());
        }
        return error("Colour Override Not Supported");
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response colour(@QueryParam("name") final String name) {
        if ( board!=null ) {
            board.setColour(name);
            return ok("Colour Changed");
        }
        return error("Colour Override Not Supported");
    }

}
