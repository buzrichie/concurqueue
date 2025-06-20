package org.example.deadlock;

public class DeadlockDemo {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("Thread 1: Holding LOCK_A...");

                try { Thread.sleep(100); } catch (InterruptedException ignored) {}

                System.out.println("Thread 1: Waiting for LOCK_B...");
                synchronized (LOCK_B) {
                    System.out.println("Thread 1: Acquired LOCK_B!");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("Thread 2: Holding LOCK_B...");

                try { Thread.sleep(100); } catch (InterruptedException ignored) {}

                System.out.println("Thread 2: Waiting for LOCK_A...");
                synchronized (LOCK_A) {
                    System.out.println("Thread 2: Acquired LOCK_A!");
                }
            }
        });

        t1.start();
        t2.start();
    }
}

