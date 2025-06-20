package org.example.producer;

import org.example.model.Task;
import org.example.model.TaskStatus;
import org.example.utils.LoggerService;
import org.example.utils.TaskQueueManager;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class TaskProducer implements Runnable {
    private final String producerName;
    private final int maxTasksPerBatch;

    public TaskProducer(String producerName, int maxTasksPerBatch) {
        this.producerName = producerName;
        this.maxTasksPerBatch = maxTasksPerBatch;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (!Thread.currentThread().isInterrupted()) {
            int tasksThisBatch = random.nextInt(maxTasksPerBatch) + 1;
            for (int i = 0; i < tasksThisBatch; i++) {
                int priority = random.nextInt(10);
                Task task = Task.builder().id(UUID.randomUUID()).name(producerName + "-Task").priority(priority)
                        .createdTimestamp(Instant.now()).payload("Payload").build();
                try {
                    TaskQueueManager.taskQueue.put(task);
                    TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.SUBMITTED);
                    LoggerService.displayLog("[Producer " + producerName + "] Submitted: " + task.getId());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LoggerService.displayLog("[Producer " + producerName + " Interrupted. Stopping producer thread.");
                    break;
                }
            }
        }
    }
}

