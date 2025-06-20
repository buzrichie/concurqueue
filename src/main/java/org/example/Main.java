package org.example;

import org.example.consumer.TaskConsumer;
import org.example.monitor.GracefulShutdown;
import org.example.monitor.TaskMonitor;
import org.example.producer.TaskProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        try (ExecutorService consumerPool = Executors.newFixedThreadPool(5)) {
            List<Thread> producers = new ArrayList<>();
            for (int i = 0; i <=2; i++) {
                producers.add(new Thread(new TaskProducer("Producer-"+(i+1))));
            }

            producers.forEach(Thread::start);

            for (int i = 0; i <5; i++) {
                consumerPool.submit(new TaskConsumer());
            }
            Thread monitor = new Thread(new TaskMonitor(consumerPool));
            monitor.start();

            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(new GracefulShutdown(producers, consumerPool)));


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}