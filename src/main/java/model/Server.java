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

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void addTask(Task newTask) {
        //System.out.println(tasks);
        tasks.add(newTask);
        waitingPeriod.getAndAdd(newTask.getServiceTime());
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                if (tasks.size() > 0) {
                    Task t = tasks.peek();
                    //System.out.println(waitingPeriod);
                    Thread.sleep(t.getServiceTime() * 100L);
                    tasks.remove();
                    waitingPeriod.getAndAdd(-t.getServiceTime());
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public BlockingQueue<Task> getTasksQueue() {
        return tasks;
    }

    @Override
    public String toString() {
        String s = "";
        for (Task t : tasks) {
            s += t.toString();
        }
        //s += "\nWaiting period: " + waitingPeriod;
        return s;
    }
}
