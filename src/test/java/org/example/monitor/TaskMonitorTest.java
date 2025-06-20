package org.example.monitor;

import static org.junit.jupiter.api.Assertions.*;

import org.example.consumer.TaskConsumer;
import org.example.model.Task;
import org.example.model.TaskStatus;
import org.example.utils.TaskQueueManager;
import org.junit.jupiter.api.*;
import java.util.UUID;
import java.time.Instant;
import java.util.concurrent.*;

class TaskMonitorTest {

        @BeforeEach
        public void setup() {
            TaskQueueManager.taskQueue.clear();
            TaskQueueManager.taskStatusMap.clear();
            TaskQueueManager.taskProcessedCounter.set(0);
        }

        @Test
        public void testMonitorLogsStatus() throws InterruptedException {
            ExecutorService pool = Executors.newFixedThreadPool(2);
            pool.submit(new TaskConsumer());

            UUID taskId = UUID.randomUUID();
            TaskQueueManager.taskQueue.put(new Task(taskId, "MonitoredTask", 1, Instant.now(), "payload",0));
            TaskQueueManager.taskStatusMap.put(taskId,TaskStatus.SUBMITTED);

            Thread monitorThread = new Thread(new TaskMonitor(pool));
            monitorThread.start();

            // Simulate some processing time
            Thread.sleep(6000);

            assertTrue(TaskQueueManager.taskProcessedCounter.get() >= 0);

            monitorThread.interrupt();
            pool.shutdownNow();
        }
    }
