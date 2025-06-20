package org.example.deadlock;

import org.example.utils.LoggerService;

public class DeadlockResolved {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> acquireLocks("Thread 1", LOCK_A, LOCK_B));
        Thread t2 = new Thread(() -> acquireLocks("Thread 2", LOCK_A, LOCK_B)); // SAME order!

        t1.start();
        t2.start();
    }

    private static void acquireLocks(String name, Object lock1, Object lock2) {
        synchronized (lock1) {
            LoggerService.displayLog(name + ": Holding lock1...");
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (lock2) {
                LoggerService.displayLog(name + ": Acquired both locks!");
            }
        }
    }
}

