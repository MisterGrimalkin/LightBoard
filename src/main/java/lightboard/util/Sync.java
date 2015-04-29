package lightboard.util;

import java.util.ArrayList;
import java.util.List;

public class Sync {

    private static Thread syncThread;

    private static List<Task> tasks = new ArrayList<>();

    private static boolean run = false;

    public static void start() {
        System.out.println("Staring Sync Thread....");
        run = true;
        syncThread = new Thread(() -> {
            System.out.println("Sync Thread Running");
            while (run) {
                tasks.forEach(Sync.Task::checkAndRun);
            }
            System.out.println("Sync Thread Stopped");
        });
        syncThread.setPriority(Thread.MAX_PRIORITY);
        syncThread.start();
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void stop() {
        run = false;
        syncThread = null;
    }

    public static abstract class Task {
        Long interval = null;
        Long lastRun = null;
        public Task(Long interval) {
            this.interval = interval;
        }
        public void checkAndRun() {
            long now = System.currentTimeMillis();
            if ( interval==null ) {
                runTask();
            } else if ( lastRun==null || now-lastRun>=interval ) {
                runTask();
                lastRun = now;
            }
        }
        public abstract void runTask();
    }

}
