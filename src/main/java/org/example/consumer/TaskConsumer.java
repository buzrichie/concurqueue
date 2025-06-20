package org.example.consumer;

import org.example.model.Task;
import org.example.model.TaskStatus;
import org.example.utils.TaskQueueManager;

import java.util.Random;

public class TaskConsumer implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Task task = TaskQueueManager.taskQueue.take();
                TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.PROCESSING);
                System.out.printf("[Consumer: %s] Processing Task %s (Priority %d)%n",
                        Thread.currentThread().getName(), task.getId(), task.getPriority());
                Thread.sleep(1000 + new Random().nextInt(3000));
                if (new Random().nextBoolean()) {
                    TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.COMPLETED);
                    TaskQueueManager.taskProcessedCounter.incrementAndGet();
                    TaskQueueManager.primitiveTaskProcessedCounter ++;
                } else {
                    task.incrementRetryCount();
                    if (task.getRetryCount() < 3) {
                        System.out.printf("[Worker: %s] RETRYING task %s (Attempt %d)\n", Thread.currentThread().getName(), task.getId(), task.getRetryCount() + 1);
                        TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.SUBMITTED);
                        TaskQueueManager.taskQueue.put(task);
                    } else {
                        System.out.printf("[Worker: %s] Task %s FAILED after 3 retries\n",
                                Thread.currentThread().getName(), task.getId());
                        TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.FAILED);
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

