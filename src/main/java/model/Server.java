package model;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    //ignore the maximum number of clients per queue
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        tasks = new SynchronousQueue<Task>();
        waitingPeriod = new AtomicInteger();
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.incrementAndGet();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task t = tasks.take();
                Thread.sleep(t.getServiceTime() * 1000L);
                waitingPeriod.decrementAndGet();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }
}
