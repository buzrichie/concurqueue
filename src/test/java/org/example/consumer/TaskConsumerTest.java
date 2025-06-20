package org.example.consumer;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.Task;
import org.example.model.TaskStatus;
import org.example.utils.TaskQueueManager;
import org.junit.jupiter.api.*;
import java.util.UUID;
import static org.awaitility.Awaitility.await;
import java.time.Instant;
import java.time.Duration;

class TaskConsumerTest {

        @BeforeEach
        public void setup() {
            TaskQueueManager.taskQueue.clear();
            TaskQueueManager.taskStatusMap.clear();
            TaskQueueManager.taskProcessedCounter.set(0);
        }

        @Test
        public void testWorkerProcessesTasks() throws InterruptedException {
            Task task = new Task(UUID.randomUUID(), "test-task", 5, Instant.now(), "payload",0);
            TaskQueueManager.taskQueue.put(task);
            TaskQueueManager.taskStatusMap.put(task.getId(), TaskStatus.SUBMITTED);

            Thread workerThread = new Thread(new TaskConsumer());
            workerThread.start();

            await().atMost(Duration.ofSeconds(10))
                    .until(() -> TaskQueueManager.taskProcessedCounter.get() > 0);

            assertTrue(TaskQueueManager.taskStatusMap.get(task.getId()) == TaskStatus.COMPLETED ||
                    TaskQueueManager.taskStatusMap.get(task.getId()) == TaskStatus.FAILED);

            workerThread.interrupt();
        }
    }
