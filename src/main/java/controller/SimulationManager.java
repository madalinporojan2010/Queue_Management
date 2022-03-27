package controller;

import model.Task;
import view.InputFrame;
import view.SimulationFrame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
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

    public SimulationManager(SelectionPolicy selectionPolicy) {
        this.inputFrame = new InputFrame();
        isInputFine = false;
        ActionListener verifyInputListener = e -> {
            isInputFine = verifyAndParseInput();
            if (!isInputFine) {
                JOptionPane.showMessageDialog(null, "CHECK INPUT!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        };
        ActionListener simulateListener = e -> {
            if (isInputFine) {
                scheduler = new Scheduler(numberOfQueues, numberOfClients, SelectionPolicy.SHORTEST_QUEUE);

                simulationFrame = new SimulationFrame();
                for (int i = 0; i < numberOfQueues; i++) {
                    Thread t = new Thread(this);
                    t.start();
                }
                tasks = generateRandomTasks();
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
        List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < numberOfClients; i++) {
            Random random = new Random();
            Task t = new Task(i, random.nextInt(minArrivalTime, maxArrivalTime + 1), random.nextInt(minServiceTime, maxServiceTime + 1));
            generatedTasks.add(t);
        }
        generatedTasks.sort(Task::compareTo);
        return generatedTasks;
    }

    public synchronized void updateSimulationFrame(Task t) {
        System.out.println(t);
        simulationFrame.getWaitingArea().append(t + "\n");
    }

    @Override
    public synchronized void run() {
        int currentTime = 0;

        // System.out.println(simulationInterval);
        while (currentTime < simulationInterval) {
            if (tasks != null) {
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(tasks.get(i));
                        tasks.remove(i);
                    }
                }
                simulationFrame.getWaitingArea().setText(tasks.toString());
                simulationFrame.getQueue1Area().setText(scheduler.getServers().get(0).toString());
                simulationFrame.getQueue2Area().setText(scheduler.getServers().get(1).toString());
                simulationFrame.getQueue3Area().setText(scheduler.getServers().get(2).toString());
                simulationFrame.getTextField1().setText(String.valueOf(currentTime));
                currentTime++;
                synchronized (this) {
                    try {
                        this.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager(SelectionPolicy.SHORTEST_QUEUE);
        Thread t = new Thread(gen);
        t.start();
    }
}
