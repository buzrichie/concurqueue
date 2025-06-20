package org.example.utils;

import org.example.model.Task;
import org.example.model.TaskStatus;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskQueueManager {
//    public static PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<Task>();
    public static final int MAX_CAPACITY = 100;
    public static final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(MAX_CAPACITY);

    public static ConcurrentHashMap<UUID, TaskStatus> taskStatusMap = new ConcurrentHashMap<java.util.UUID, TaskStatus>();
    public static AtomicInteger taskProcessedCounter = new AtomicInteger();
    public static int primitiveTaskProcessedCounter;
}
