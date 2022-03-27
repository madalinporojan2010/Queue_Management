package model;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        tasks = new ArrayBlockingQueue<>(1000);
        waitingPeriod = new AtomicInteger();
    }

    public void addTask(Task newTask) {
        System.out.println(tasks);
        tasks.add(newTask);
        waitingPeriod.getAndIncrement();
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                if (tasks.size() > 0) {
                    Task t = tasks.peek();
                    System.out.println(waitingPeriod);
                    Thread.sleep(t.getServiceTime() * 1000L);
                    tasks.remove();
                    waitingPeriod.getAndDecrement();
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    @Override
    public String toString() {
        String s = "";
        for (Task t : tasks) {
            s += t.toString() + "\n";
        }
        //s += "\nWaiting period: " + waitingPeriod;
        return s;
    }
}
