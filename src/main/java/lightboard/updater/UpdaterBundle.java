package lightboard.updater;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grimalkin on 03/04/15.
 */
public class UpdaterBundle {

    public static UpdaterBundle bundle(Updater... updaters) {
        return new UpdaterBundle(updaters);
    }

    private List<Updater> updaters = new ArrayList<>();

    private UpdaterBundle(Updater... upds) {
        for ( int i=0; i<upds.length; i++ ) {
            updaters.add(upds[i]);
        }
    }

    public void start() {
        start(Updater.DATA_REFRESH, true);
    }

    public void start(int dataRefresh) {
        start(dataRefresh, true);
    }

    public void start(int dataRefresh, boolean refreshOnStart) {
        Timeline updateData = new Timeline(new KeyFrame(
                Duration.millis(dataRefresh),
                ae -> refresh()));
        updateData.setCycleCount(Animation.INDEFINITE);
        updateData.play();
        if ( refreshOnStart ) {
            refresh();
        }
    }

    public void refresh() {
        for( Updater updater : updaters ) {
            updater.refresh();
        }
    }


}
