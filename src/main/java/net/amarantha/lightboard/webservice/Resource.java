package net.amarantha.lightboard.webservice;

import javax.ws.rs.core.Response;

public class Resource {

    protected Response ok(String entity) {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(entity)
                .build();
    }

    protected Response error(String entity) {
        return Response.serverError()
                .header("Access-Control-Allow-Origin", "*")
                .entity(entity)
                .build();
    }


}
