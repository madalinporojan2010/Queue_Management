package mvc.model;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private AtomicInteger totalServiceTime;
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(int maxTasks) {
        tasks = new ArrayBlockingQueue<>(maxTasks);
        waitingPeriod = new AtomicInteger();
        totalServiceTime = new AtomicInteger();
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.getAndAdd(1);
        totalServiceTime.getAndAdd(newTask.getServiceTime());
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                if (tasks.size() > 0) {
                    Task t = tasks.peek();
                    Thread.sleep(t.getServiceTime() * 100L);
                    tasks.remove();
                    waitingPeriod.getAndAdd(-1);
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
        if (!s.equals(""))
            return s.substring(0, s.lastIndexOf("\n"));
        return s;
    }
}
