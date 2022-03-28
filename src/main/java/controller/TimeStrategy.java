package controller;

import model.Server;
import model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        int minimumWaitingTime = servers.get(0).getWaitingPeriod().get();
        int minIndex = 0;
        for (Server s : servers) {
            if (minimumWaitingTime > s.getWaitingPeriod().get()) {
                minimumWaitingTime = s.getWaitingPeriod().get();
                minIndex = servers.indexOf(s);
            }
        }
        servers.get(minIndex).addTask(task);
    }

}
