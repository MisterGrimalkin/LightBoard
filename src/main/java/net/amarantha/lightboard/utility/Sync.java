package net.amarantha.lightboard.utility;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.NORM_PRIORITY;

@Singleton
public class Sync {

    private Thread syncThread;

    protected Map<Integer, Task> tasks = new HashMap<>();

    protected boolean run = false;

    public void init() {
        tasks = new HashMap<>();
    }

    public void startSyncThread() {
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

    public void startTimerTask(TimerTask task, Long interval) {
        Timer timer = new Timer();
        timer.schedule(task, 0, interval);
    }

    private static int nextTask = 0;

    public int addTask(Task task) {
        tasks.put(nextTask, task);
        return nextTask++;
    }

    public void resumeTask(int id) {
        Task task = tasks.get(id);
        if ( task!=null ) {
            task.active = true;
        }
    }

    public void pauseTask(int id) {
        Task task = tasks.get(id);
        if ( task!=null ) {
            task.active = false;
        }
    }

    public void stopSyncThread() {
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
        public void setInterval(Long interval) {
            this.interval = interval;
        }
        public abstract void runTask();
    }

}
