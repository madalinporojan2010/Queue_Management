package mvc.controller;

import mvc.model.Server;
import mvc.model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        int minimumWaitingTime = servers.get(0).getWaitingPeriod().get();
        int minIndex = 0;
        for (Server server : servers) {
            if (minimumWaitingTime > server.getWaitingPeriod().get()) {
                minimumWaitingTime = server.getWaitingPeriod().get();
                minIndex = servers.indexOf(server);
            }
        }
        servers.get(minIndex).addTask(task);
    }

}
