package mvc.controller;

import mvc.model.Server;
import mvc.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    private int maxNoServers;
    private int maxTasksPerServer;
    private float avgWaitingTime;
    private float maxWaitingTime;
    private float avgServiceTime;

    private float finalAvgWaitingTime;


    private float finalAvgServiceTime;
    private int peakHour;

    private List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer, SelectionPolicy selectionPolicy) {
        servers = Collections.synchronizedList(new ArrayList<>());
        this.maxTasksPerServer = maxTasksPerServer;
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(maxTasksPerServer);
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
        this.maxNoServers = maxNoServers;
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
            if (s.getTasksQueue().size() > 0) {
                return false;
            }
        }
        return true;
    }

    public float getFinalAvgServiceTime() {
        return finalAvgServiceTime;
    }

    public float getFinalAvgWaitingTime() {
        return finalAvgWaitingTime;
    }

    public float getAvgWaitingTime() {
        avgWaitingTime = 0;
        for (Server s : servers) {

            avgWaitingTime += (float) s.getWaitingPeriod().get();
        }
        avgWaitingTime /= maxNoServers;
        finalAvgWaitingTime += avgWaitingTime;
        return avgWaitingTime;
    }

    public float getAvgServiceTimePerQueues() {
        avgServiceTime = 0;
        for (Server s : servers) {
            if (s.getTasksQueue().size() > 0)
                avgServiceTime += s.getTotalServiceTime().get() * 1.0f / s.getTasksQueue().size();
            else
                avgServiceTime += s.getTotalServiceTime().get();
        }

        avgServiceTime /= maxNoServers;
        finalAvgServiceTime += avgServiceTime;
        return avgServiceTime;
    }

    public int getPeakHour(int currentTime) {
        if (maxWaitingTime < avgWaitingTime) {
            maxWaitingTime = avgWaitingTime;
            peakHour = currentTime;
        }
        return peakHour;
    }
}
