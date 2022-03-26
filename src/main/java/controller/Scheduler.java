package controller;

import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            Thread thread = new Thread(server);
            servers.add(server);
        }
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }
}
