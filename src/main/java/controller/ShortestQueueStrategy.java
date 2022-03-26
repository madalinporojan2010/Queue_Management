package controller;

import model.Server;
import model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        int minNOfClients = servers.get(0).getTasks().length;
        for(Server s : servers) {
            if(minNOfClients > s.getTasks().length) {
                s.addTask(task);
                break;
            }
        }
    }
}
