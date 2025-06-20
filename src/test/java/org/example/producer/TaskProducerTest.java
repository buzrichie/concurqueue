package org.example.producer;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.TaskStatus;
import org.example.utils.TaskQueueManager;
import org.junit.jupiter.api.*;
import static org.awaitility.Awaitility.await;
import java.time.Duration;

public class TaskProducerTest {

    @BeforeEach
    public void setup() {
        TaskQueueManager.taskQueue.clear();
        TaskQueueManager.taskStatusMap.clear();
    }

    @Test
    public void testProducerAddsTasksToQueue() {
        Thread producerThread = new Thread(new TaskProducer("TEST-PRODUCER",7));
        producerThread.start();

        await().atMost(Duration.ofSeconds(5))
                .until(() -> !TaskQueueManager.taskQueue.isEmpty());

        assertFalse(TaskQueueManager.taskQueue.isEmpty());
        assertEquals(TaskStatus.SUBMITTED,
                TaskQueueManager.taskStatusMap
                        .values().iterator().next());

        producerThread.interrupt();
    }
}
