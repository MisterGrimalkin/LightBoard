package lightboard.updater;

import lightboard.zone.impl.TextZone;

import java.util.Timer;
import java.util.TimerTask;

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

    public void start() {
        start(DATA_REFRESH, true);
    }

    public void start(int dataRefresh) {
        start(dataRefresh, true);
    }

    public void start(int dataRefresh, boolean refreshOnStart) {
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, (refreshOnStart ? 10 : dataRefresh), dataRefresh);
    }

    public void fireOnce(int delay) {
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, delay+10);
    }


    //////////////////////////
    // Messages to TextZone //
    //////////////////////////

    protected void replaceMessage(String... messages) {
        clearMessages();
        addMessage(messages);
    }

    protected void clearMessages() {
        zone.clearMessages(id);
    }

    protected void addMessage(String... messages) {
        for ( int i=0; i<messages.length; i++ ) {
            zone.addMessage(id, messages[i]);
        }
    }


    protected final int id;
    public static int nextId = 0;

    public final static int DATA_REFRESH = 60000;

}
