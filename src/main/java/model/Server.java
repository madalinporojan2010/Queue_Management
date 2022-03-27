package model;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private AtomicInteger totalServiceTime;
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        tasks = new ArrayBlockingQueue<>(1000);
        waitingPeriod = new AtomicInteger();
        totalServiceTime = new AtomicInteger();
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void addTask(Task newTask) {
        //System.out.println(tasks);
        tasks.add(newTask);
        waitingPeriod.getAndAdd(newTask.getServiceTime());
        totalServiceTime.getAndAdd(newTask.getServiceTime());
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
                    totalServiceTime.getAndAdd(-t.getServiceTime());
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public AtomicInteger getTotalServiceTime() {
        return totalServiceTime;
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
        if(!s.equals(""))
            return s.substring(0, s.lastIndexOf("\n"));
        return s;
    }
}
