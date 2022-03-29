package mvc.controller;

import mvc.model.Server;
import mvc.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        int minNOfClients = servers.get(0).getTasksQueue().size();
        int minIndex = 0;
        for (Server server : servers) {
            if (minNOfClients > server.getTasksQueue().size()) {
                minNOfClients = server.getTasksQueue().size();
                minIndex = servers.indexOf(server);
            }
        }
        servers.get(minIndex).addTask(task);
    }
}
