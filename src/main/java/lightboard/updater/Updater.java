package lightboard.updater;

import lightboard.board.zone.impl.TextZone;
import lightboard.util.Sync;

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

    public void start(int dataRefresh) {
        Sync.addTask(new Sync.Task((long)dataRefresh) {
            @Override
            public void runTask() {
                refresh();
            }
        });
        System.out.println("Updater running every " + dataRefresh + "ms");
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
        for (String message : messages) {
            zone.addMessage(id, message);
        }
    }

    protected final int id;
    public static int nextId = 0;

}
