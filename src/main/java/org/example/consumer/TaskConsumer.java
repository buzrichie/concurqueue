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
                System.out.printf("[Worker: %s] Processing Task %s (Priority %d)%n",
                        Thread.currentThread().getName(), task.getId(), task.getPriority());
                Thread.sleep(1000 + new Random().nextInt(3000));
                if (new Random().nextBoolean()) {
                    TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.COMPLETED);
                } else {
                    TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.FAILED);
                }
                TaskQueueManager.taskProcessedCounter.incrementAndGet();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

