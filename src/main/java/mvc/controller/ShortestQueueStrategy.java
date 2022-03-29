package mvc.controller;

import mvc.model.Server;
import mvc.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        int minNOfClients = servers.get(0).getTasksQueue().size();
        int minIndex = 0;
        for (Server s : servers) {
            if (minNOfClients > s.getTasksQueue().size()) {
                minNOfClients = s.getTasksQueue().size();
                minIndex = servers.indexOf(s);
            }
        }
        servers.get(minIndex).addTask(task);
    }
}
