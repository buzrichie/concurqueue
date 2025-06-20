package org.example.consumer;

import org.example.model.Task;
import org.example.model.TaskStatus;
import org.example.utils.TaskQueueManager;
import org.example.utils.LoggerService;

import java.util.Random;

public class TaskConsumer implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Task task = TaskQueueManager.taskQueue.take();
                TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.PROCESSING);

                LoggerService.displayLog(String.format("[Consumer: %s] Processing Task %s (Priority %d)",
                        Thread.currentThread().getName(), task.getId(), task.getPriority()));

                Thread.sleep(1000 + new Random().nextInt(3000));

                if (new Random().nextBoolean()) {
                    TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.COMPLETED);
                    TaskQueueManager.taskProcessedCounter.incrementAndGet();
                    TaskQueueManager.primitiveTaskProcessedCounter++;
                } else {
                    task.incrementRetryCount();
                    if (task.getRetryCount() < 3) {
                        LoggerService.displayLog(String.format("[Worker: %s] RETRYING task %s (Attempt %d)",
                                Thread.currentThread().getName(), task.getId(), task.getRetryCount() + 1));

                        TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.SUBMITTED);
                        TaskQueueManager.taskQueue.put(task);
                    } else {
                        LoggerService.displayLog(String.format("[Worker: %s] Task %s FAILED after 3 retries",
                                Thread.currentThread().getName(), task.getId()));
                        TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.FAILED);
                    }
                }
            } catch (InterruptedException e) {
                LoggerService.displayLog(String.format("[Consumer: %s] Interrupted. Exiting...",
                        Thread.currentThread().getName()));
                break;
            }
        }
    }
}
