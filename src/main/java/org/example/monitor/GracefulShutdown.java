package org.example.monitor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class GracefulShutdown implements Runnable {
    private final List<Thread> producers;
    private final ExecutorService workerPool;

    public GracefulShutdown(List<Thread> producers, ExecutorService workerPool) {
        this.producers = producers;
        this.workerPool = workerPool;
    }

    @Override
    public void run() {
        System.out.println("Shutdown hook triggered...");

        // Step 1: Stop producers
        producers.forEach(Thread::interrupt); // Interrupt each producer thread

        for (Thread producer : producers) {
            try {
                producer.join(); // Wait for each producer to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while waiting for producer to finish: " + producer.getName());
            }
        }

        // Step 2: Shutdown the worker pool
        workerPool.shutdown(); // No new tasks will be accepted
        try {
            if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Forcing shutdown of worker pool...");
                workerPool.shutdownNow(); // Cancel currently running tasks
            }
        } catch (InterruptedException e) {
            workerPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Shutdown complete.");
    }
}
