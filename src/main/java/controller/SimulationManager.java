package controller;

import model.Task;
import view.InputFrame;
import view.SimulationFrame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable{
    private boolean isInputFine;
    private int numberOfClients;
    private int numberOfQueues;
    private int simulationInterval;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;

    private Scheduler scheduler;
    private InputFrame inputFrame;
    private SimulationFrame simulationFrame;
    private List<Task> tasks;
    private SelectionPolicy selectionPolicy;

    public SimulationManager(SelectionPolicy selectionPolicy, InputFrame inputFrame) {
        this.inputFrame = inputFrame;
        isInputFine = false;
        ActionListener verifyInputListener = e -> {
            isInputFine = verifyAndParseInput();
            if (!isInputFine) {
                JOptionPane.showMessageDialog(null, "CHECK INPUT!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        };
        ActionListener simulateListener = e -> {
            if (isInputFine) {
                scheduler = new Scheduler(numberOfQueues, numberOfClients);
                this.selectionPolicy = selectionPolicy;
                tasks = generateRandomTasks();
                simulationFrame = new SimulationFrame();
                inputFrame.getFrame().dispose();
            } else {
                JOptionPane.showMessageDialog(null, "CHECK INPUT!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        };
        inputFrame.getValidateInputButton().addActionListener(verifyInputListener);
        inputFrame.getStartSimButton().addActionListener(simulateListener);
    }

    public boolean verifyAndParseInput() {
        try {
            numberOfClients = Integer.parseInt(inputFrame.getnOfClientsField().getText());
            numberOfQueues = Integer.parseInt(inputFrame.getnOfQueuesField().getText());
            simulationInterval = Integer.parseInt(inputFrame.getSimIntervalField().getText());
            minArrivalTime = Integer.parseInt(inputFrame.getMinArrivalTimeField().getText());
            maxArrivalTime = Integer.parseInt(inputFrame.getMaxArrivalTimeField().getText());
            minServiceTime = Integer.parseInt(inputFrame.getMinServiceTimeField().getText());
            maxServiceTime = Integer.parseInt(inputFrame.getMaxServiceTimeField().getText());
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return (minArrivalTime < maxArrivalTime) && (minServiceTime < maxServiceTime) &&
                (minServiceTime > 0) && (minArrivalTime > 0) && numberOfClients > 0 && numberOfQueues > 0
                && numberOfQueues <= numberOfClients;
    }

    public List<Task> generateRandomTasks() {
        List<Task> generatedTasks = new ArrayList<>();
        for(int i = 0; i < numberOfClients; i++) {
            Random random = new Random();
            Task t = new Task(i, random.nextInt(simulationInterval - 1), random.nextInt(minServiceTime, maxServiceTime));
            generatedTasks.add(t);
        }
        generatedTasks.sort(Task::compareTo);
        return generatedTasks;
    }

    @Override
    public void run() {
        int currentTime = 0;
        while (currentTime < simulationInterval) {
            for (Task t : tasks) {
                if(t.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(t);
                    tasks.remove(t);
                    //update frame method
                }
            }
            currentTime++;
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setInputFine(boolean inputFine) {
        isInputFine = inputFine;
    }

    public static void main(String[] args) {
        InputFrame inputFrame = new InputFrame();
        SimulationManager gen = new SimulationManager(SelectionPolicy.SHORTEST_QUEUE, inputFrame);

        Thread t = new Thread(gen);
        t.start();
    }
}
