package org.example.producer;

import org.example.model.Task;
import org.example.model.TaskStatus;
import org.example.utils.TaskQueueManager;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class TaskProducer implements Runnable {
    private final String producerName;

    public TaskProducer(String producerName) {
        this.producerName = producerName;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (!Thread.currentThread().isInterrupted()) {
            int priority = random.nextInt(10);
            Task task = new Task(UUID.randomUUID(), producerName + "-Task", priority, Instant.now(), "Payload");
            TaskQueueManager.taskQueue.put(task);
            TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.SUBMITTED);
            System.out.println("[Producer " + producerName + "] Submitted: " + task.getId());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

