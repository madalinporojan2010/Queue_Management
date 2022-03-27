package controller;

import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    private int maxNoServers;
    private int maxTasksPerServer;
    private float avgWaitingTime;
    private float maxAvgWaitingTime;
    private float avgServiceTime;
    private int peakHour;


    private List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer, SelectionPolicy selectionPolicy) {
        servers = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        changeStrategy(selectionPolicy);
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        } else if (selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

    public boolean areServersEmpty() {
        for (Server s : servers) {
            if (s.getTasksQueue().size() > 0)
                return false;
        }
        return true;
    }

    public float getAvgWaitingTime() {
        avgWaitingTime = 0;
        for (Server s : servers) {
            avgWaitingTime += s.getWaitingPeriod().get();
        }
        avgWaitingTime /= maxNoServers;
        return avgWaitingTime;
    }

    public float getAvgServiceTimePerQueues() {
        avgServiceTime = 0;
        for (Server s : servers) {
            if(s.getTasksQueue().size() > 0)
                avgServiceTime += s.getTotalServiceTime().get() * 1.0f / s.getTasksQueue().size();
            else
                avgServiceTime += s.getTotalServiceTime().get();
        }
        avgServiceTime /= maxNoServers;
        return avgServiceTime;
    }

    public int getPeakHour(int currentTime) {
        if (maxAvgWaitingTime < avgWaitingTime) {
            maxAvgWaitingTime = avgWaitingTime;
            peakHour = currentTime;
        }
        return peakHour;
    }
}
