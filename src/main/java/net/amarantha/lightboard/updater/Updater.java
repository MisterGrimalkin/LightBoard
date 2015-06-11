package net.amarantha.lightboard.updater;

import net.amarantha.lightboard.util.Sync;
import net.amarantha.lightboard.zone.impl.TextZone;

public abstract class Updater {

    protected TextZone zone;

    protected Updater(TextZone zone) {
        this.zone = zone;
        id = nextId++;
    }

    public abstract void refresh();


    //////////////////
    // Update Timer //
    //////////////////

    private Long dataRefresh = 10000L;

    public Updater setDataRefresh(int dataRefresh) {
        return setDataRefresh((long)dataRefresh);
    }

    public Updater setDataRefresh(Long dataRefresh) {
        this.dataRefresh = dataRefresh;
        return this;
    }

    public void start() {
        Sync.addTask(new Sync.Task(dataRefresh) {
            @Override
            public void runTask() {
                doRefresh();
            }
        });
        System.out.println("Updater running every " + dataRefresh + "ms");
    }

    private boolean paused = true;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        doRefresh();
    }

    private void doRefresh() {
        if ( !paused ) {
            refresh();
        }
    }

    //////////////////////////
    // Messages to TextZone //
    //////////////////////////

    protected void replaceMessage(String... messages) {
        clearMessages();
        addMessage(messages);
    }

    protected void clearMessages() {
        if ( zone!=null ) {
            zone.clearMessages(id);
        }
    }

    protected void addMessage(String... messages) {
        if ( zone!=null ) {
            for (String message : messages) {
                zone.addMessage(id, message);
            }
        }
    }

    protected final int id;
    public static int nextId = 0;

}
