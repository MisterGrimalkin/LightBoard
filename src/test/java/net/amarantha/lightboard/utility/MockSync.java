package net.amarantha.lightboard.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class MockSync extends Sync {

    @Override
    public void startSyncThread() {
        System.out.println("Staring Mock Sync Thread with " + tasks.size() + " tasks");
        System.out.println("   and " + timerTasks.size() + " timer tasks");
        run = true;
    }

    public void runTasks() {
        for ( Map.Entry<Integer, Task> entry : tasks.entrySet() ) {
            entry.getValue().runTask();
        }
    }

    public void runTimerTasks() {
        timerTasks.forEach(java.util.TimerTask::run);
    }

    private List<TimerTask> timerTasks = new ArrayList<>();

    @Override
    public void startTimerTask(TimerTask task, Long interval) {
        timerTasks.add(task);
    }
}
