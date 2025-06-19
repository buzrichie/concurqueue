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

                LoggerService.displayLog("[Monitor] Queue size: " + TaskQueueManager.taskQueue.size()
                        + " Tasks processed: " + TaskQueueManager.taskProcessedCounter.get()+
                        " Tasks processed Primitive " + TaskQueueManager.taskProcessedCounter+  " Active Pool(Consumers): " + ((ThreadPoolExecutor) poolExecutor).getActiveCount());
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
