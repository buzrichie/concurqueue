package org.example.monitor;

import org.example.utils.LoggerService;
import org.example.utils.TaskQueueManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskMonitor implements Runnable {

    ExecutorService poolExecutor;


    public TaskMonitor(ExecutorService executor) {
        this.poolExecutor = executor;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {

                ThreadPoolExecutor executor = (ThreadPoolExecutor) poolExecutor;

                LoggerService.displayLog("[Monitor] " +
                        "Queue size: " + TaskQueueManager.taskQueue.size() +
                        " | Remaining Capacity: " + TaskQueueManager.taskQueue.remainingCapacity() +
                        " | Tasks processed: " + TaskQueueManager.taskProcessedCounter.get() +
                        " | Raw Processed: " + TaskQueueManager.taskProcessedCounter +
                        " | Active Pool (Consumers): " + executor.getActiveCount() +
                        " | Idle Threads: " + (executor.getPoolSize() - executor.getActiveCount()) +
                        " | Total Tasks Submitted: " + executor.getTaskCount()
                );
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
