package model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    //we need to display in realtime only the customers that are in the queue

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
}
