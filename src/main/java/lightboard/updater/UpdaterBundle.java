package lightboard.updater;

import lightboard.util.Sync;

import java.util.ArrayList;
import java.util.Collections;
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
        Collections.addAll(updaters, upds);
    }

    public void start(int dataRefresh) {
        Sync.addTask(new Sync.Task((long) dataRefresh) {
            @Override
            public void runTask() {
                refresh();
            }
        });
        System.out.println("UpdaterBundle running every " + dataRefresh + "ms");
    }

    public void refresh() {
        updaters.forEach((updater) -> updater.refresh());
    }


}
