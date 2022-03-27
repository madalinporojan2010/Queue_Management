package controller;

import model.Task;

import java.util.ArrayList;

public class CustomArrayList extends ArrayList<Task> {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        String result = "";
        for(Task t : this) {
            result += t;
        }
        return result;
    }
}
