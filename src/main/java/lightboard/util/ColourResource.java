package lightboard.util;

import lightboard.board.HasColourSwitcher;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

@Path("colour")
public class ColourResource {

    private static List<HasColourSwitcher> boards = new ArrayList<>();

    public static <T extends HasColourSwitcher> void addBoard(T board) {
        boards.add(board);
    }

    @POST
    @Path("red")
    public void red() {
        boards.forEach(HasColourSwitcher::red);
    }

    @POST
    @Path("green")
    public void green() {
        boards.forEach(HasColourSwitcher::green);
    }

    @POST
    @Path("yellow")
    public void yellow() {
        boards.forEach(HasColourSwitcher::yellow);
    }

    @POST
    @Path("blue")
    public void blue() {
        boards.forEach(HasColourSwitcher::blue);
    }

    @POST
    @Path("cycle")
    public void cycle(@QueryParam("time") int time) {
        boards.forEach((hasColourSwitcher) -> hasColourSwitcher.cycle(time));
    }

}
