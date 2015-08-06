package net.amarantha.lightboard.updater;

import net.amarantha.lightboard.utility.Sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdaterBundle extends Updater {

    public static UpdaterBundle bundle(Updater... updaters) {
        return new UpdaterBundle(updaters);
    }

    private List<Updater> updaters = new ArrayList<>();

    private UpdaterBundle(Updater... upds) {
        super(null);
        Collections.addAll(updaters, upds);
    }

    private Long dataRefresh = 10000L;

    @Override
    public Updater setDataRefresh(int dataRefresh) {
        return setDataRefresh((long)dataRefresh);
    }

    @Override
    public Updater setDataRefresh(Long dataRefresh) {
        this.dataRefresh = dataRefresh;
        return this;
    }

    @Override
    public void start() {
        Sync.addTask(new Sync.Task(dataRefresh) {
            @Override
            public void runTask() {
                doRefresh();
            }
        });
        System.out.println("UpdaterBundle running every " + dataRefresh + "ms");
    }

    private boolean paused = false;

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
        doRefresh();
    }

    private void doRefresh() {
        if ( !paused ) {
            refresh();
        }
    }

    public void refresh() {
        updaters.forEach((updater) -> updater.refresh());
    }


}
