package net.amarantha.lightboard.scene;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.module.ApplicationTestModule;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.MockSync;
import net.amarantha.lightboard.utility.Sync;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static net.amarantha.lightboard.utility.MessageQueue.DEFAULT_MESSAGE;

@RunWith(StoryRunner.class) @Modules(ApplicationTestModule.class)
public class TravelInformationSceneTest {

    @Inject private LightBoardSurface surface;
    @Inject private SceneManager sceneManager;
    @Inject private TravelInformationScene scene;
    @Inject private Sync sync;


    @Story
    @Ignore
    public void testScene() {

        given_the_lighboard();

        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(1, "1", "A Place", 0, 3);
//        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(2, "1", "Other Place", 1, 7);

        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(2, "2", "There", 1, 7);
//        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(1, "2", "Back Again", 1, 7);

        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(4, "3", "Up", 1, 7);
//        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(5, "3", "Down", 1, 7);

//        given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(2, "5", "Nowhere Fast", 1, 7);

//        then_all_zones_have_$1_messages(1); // Default message

        ((MockSync) sync).runTimerTasks();

//        then_all_zones_have_$1_messages(3);

        for ( int i=0; i<100; i++ ) {
            ((MockSync) sync).runTasks();
//            scene.getBusNumber().advanceMessage();
//            scene.getBusDestinationLeft().advanceMessage();
//            scene.getBusDestinationRight().advanceMessage();
//            scene.getBusTimesLeft().advanceMessage();
//            scene.getBusTimesRight().advanceMessage();
            printZoneContents();
        }

    }

    void then_all_zones_have_$1_messages(int count) {
        Assert.assertEquals(count, scene.getBusNumber().countMessages());
        Assert.assertEquals(count, scene.getBusDestinationLeft().countMessages());
        Assert.assertEquals(count, scene.getBusTimesLeft().countMessages());
        Assert.assertEquals(count, scene.getBusDestinationRight().countMessages());
        Assert.assertEquals(count, scene.getBusTimesRight().countMessages());
    }

    void given_no_buses() {
        ((MockBusUpdater)scene.getBusUpdater()).clearBuses();
    }

    String given_stop_$1__bus_$2_to_$3_every_$5_mins_starting_in_$4_mins(long stop, String bus, String destination, int delay, int frequency) {
        return ((MockBusUpdater)scene.getBusUpdater()).addBus(stop, bus, destination, delay, frequency);
    }

    void given_the_lighboard() {
        sync.init();
        surface.init(32, 192);

        scene.build();
        scene.testMode();
        scene.start();
        scene.resume();

        sync.startSyncThread();
    }

    private boolean printZoneContents() {
        if ( !scene.getBusNumber().getCurrentMessage().getMessage().equals(DEFAULT_MESSAGE.getMessage())) {
            System.out.print(stripTags(scene.getBusNumber().getCurrentMessage().getMessage()));
            System.out.print("  " + stripTags(scene.getBusDestinationLeft().getCurrentMessage().getMessage()));
            System.out.print(" " + stripTags(scene.getBusTimesLeft().getCurrentMessage().getMessage()));
            System.out.print("  " + stripTags(scene.getBusDestinationRight().getCurrentMessage().getMessage()));
            System.out.println(" " + stripTags(scene.getBusTimesRight().getCurrentMessage().getMessage()));
            System.out.println();

            return true;
        }
        return false;
    }

    private String stripTags(String input) {
        return input.replaceAll("\\{\\w*\\}", "");
    }


}
