package mvc.controller;

import mvc.model.Server;
import mvc.model.Task;

import java.util.List;

public interface Strategy {
    void addTask(List<Server> servers, Task task);
}
