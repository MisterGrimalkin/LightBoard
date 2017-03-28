package net.amarantha.lightboard.updater;

import net.amarantha.lightboard.utility.Sync;

import java.util.TimerTask;

public abstract class Updater {

//    protected TextZone zone;

    protected final Sync sync;

    protected Updater(Sync sync) {
        this.sync = sync;
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
        sync.startTimerTask(new TimerTask() {
            @Override
            public void run() {
                doRefresh();
            }
        }, dataRefresh);
        System.out.println(getClass().getSimpleName() + " running every " + dataRefresh + "ms");
        paused = false;
    }

    private boolean paused = true;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        doRefresh();
    }

    protected void doRefresh() {
        if ( !paused ) {
            try {
                long now = System.currentTimeMillis();
                System.out.println("Refreshing " + getClass().getSimpleName() + "...");
                refresh();
                System.out.println("Refreshed " + getClass().getSimpleName() + " in " + (System.currentTimeMillis()-now) + "ms");
            } catch ( Exception e ) {
                System.out.println("ERROR!\n"+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //////////////////////////
    // Messages to TextZone //
    //////////////////////////

//    protected void replaceMessage(String... messages) {
//        clearMessages();
//        addMessage(messages);
//    }
//
//    protected void clearMessages() {
//        if ( zone!=null ) {
//            zone.clearMessages(id);
//        }
//    }
//
//    protected void addMessage(String... messages) {
//        if ( zone!=null ) {
//            for (String message : messages) {
//                zone.addMessage(id, message);
//            }
//        }
//    }

    protected final int id;
    public static int nextId = 0;

}
