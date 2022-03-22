package model;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    //ignore the maximum number of clients per queue
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(AtomicInteger waitingPeriod) {
        tasks = new SynchronousQueue<>();
    }

    public void addTask(Task newTask) {

    }
}
