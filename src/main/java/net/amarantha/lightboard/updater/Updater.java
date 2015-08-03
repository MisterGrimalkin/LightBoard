package net.amarantha.lightboard.updater;

import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.MIN_PRIORITY;

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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doRefresh();
            }
        }, 0, dataRefresh);
//        Sync.addTask(new Sync.Task(dataRefresh) {
//            @Override
//            public void runTask() {
//                doRefresh();
//            }
//        });
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
//            Thread update = new Thread(() -> {

                try {
                    refresh();
                } catch ( Exception e ) {
                    System.out.println("ERROR!\n"+e.getMessage());
                    e.printStackTrace();
                }
//            });
//            update.setPriority(MIN_PRIORITY);
//            update.start();
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
