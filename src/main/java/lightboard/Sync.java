package lightboard;

import java.util.ArrayList;
import java.util.List;

public class Sync {

    private static Thread syncThread;

    private static List<Task> tasks = new ArrayList<>();

    private static boolean run = false;

    public static void start() {
        run = true;
        syncThread = new Thread(() -> {
            System.out.println("Staring Sync Thread... " + tasks.size());
            while (run) {
                for ( Task task : tasks ) {
                    task.checkAndRun();
                }
            }
//            System.out.println("Stopped Sync Thread");
        });
        syncThread.setPriority(Thread.MAX_PRIORITY);
        syncThread.start();
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void stop() {
//        syncThread.stop();
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
            if ( interval==null || lastRun==null || now-lastRun>=interval ) {
                runTask();
                lastRun = now;
            }
        }
        public abstract void runTask();
    }

}
