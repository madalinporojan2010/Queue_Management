package controller;

import model.Server;
import model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        int minNOfClients = servers.get(0).getTasks().size();
        int minIndex = 0;
        for(Server s : servers) {
            if(minNOfClients > s.getTasks().size()) {
                minNOfClients = s.getTasks().size();
                minIndex = servers.indexOf(s);
            }
        }
        servers.get(minIndex).addTask(task);
    }
}
