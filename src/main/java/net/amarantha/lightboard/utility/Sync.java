package net.amarantha.lightboard.utility;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.NORM_PRIORITY;

public class Sync {

    private static Thread syncThread;

//    private static Task priorityTask;

    private static Map<Integer, Task> tasks = new HashMap<>();

    private static boolean run = false;

    public static void startSyncThread() {
        System.out.println("Staring Sync Thread with " + tasks.size() + " tasks....");
        run = true;
        syncThread = new Thread() {
            @Override
            public void run() {
            System.out.println("Sync Thread Running");
            while (run) {
                for ( Map.Entry<Integer, Task> entry : tasks.entrySet() ) {
                    entry.getValue().checkAndRun();
                }
            }
            System.out.println("Sync Thread Stopped");
        }};
        syncThread.setPriority(NORM_PRIORITY);
        syncThread.start();
    }

    private static int nextTask = 0;

    public static int addTask(Task task) {
        tasks.put(nextTask, task);
        return nextTask++;
    }

    public static void resumeTask(int id) {
        Task task = tasks.get(id);
        if ( task!=null ) {
            task.active = true;
        }
    }

    public static void pauseTask(int id) {
        Task task = tasks.get(id);
        if ( task!=null ) {
            task.active = false;
        }
    }

    public static void stopSyncThread() {
        run = false;
        syncThread = null;
    }

    public static abstract class Task {
        private boolean active = true;
        private Long interval = null;
        private Long lastRun = null;
        public Task(Long interval) {
            this.interval = interval;
        }
        public void checkAndRun() {
            if ( active ) {
                long now = System.currentTimeMillis();
                if (interval == null) {
                    runTask();
                } else if (lastRun == null || now - lastRun >= interval) {
                    runTask();
                    lastRun = now;
                }
            }
        }
        public abstract void runTask();
    }

}
