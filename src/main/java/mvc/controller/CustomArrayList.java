package mvc.controller;

import mvc.model.Task;

import java.util.ArrayList;

public class CustomArrayList extends ArrayList<Task> {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        String result = "";
        for (Task task : this) {
            result += task;
        }
        return result;
    }
}
