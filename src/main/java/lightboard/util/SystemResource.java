package lightboard.util;

import lightboard.scene.SceneManager;
import lightboard.updater.WebService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Timer;
import java.util.TimerTask;

@Path("system")
public class SystemResource {

    @POST
    @Path("shutdown")
    public String shutdown() {
        System.out.println("Shutting Down....");
        WebService.stopWebService();
        Sync.stopSyncThread();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 5000);
        return "So long!";
    }

    @POST
    @Path("sleep")
    public void sleep() {
        System.out.println("Sleeping");
        SceneManager.sleep();
    }

    @POST
    @Path("wake")
    public void wake() {
        System.out.println("Waking");
        SceneManager.wake();
    }

}
