package controller;

import model.Server;
import model.Task;
import view.InputFrame;
import view.SimulationFrame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

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

    public SimulationManager() {
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
                scheduler = new Scheduler(numberOfQueues, numberOfClients, selectionPolicy);

                simulationFrame = new SimulationFrame();

                tasks = generateRandomTasks();
                inputFrame.getFrame().dispose();

                ActionListener closeSimulationListener = close -> {
                    System.exit(0);
                };
                if (simulationFrame != null)
                    simulationFrame.getCloseSimButton().addActionListener(closeSimulationListener);

                Thread t = new Thread(this);
                t.start();
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
            if (inputFrame.getShortestQueueStrategyRadioButton().isSelected()) {
                selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
            } else {
                selectionPolicy = SelectionPolicy.SHORTEST_TIME;
            }
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return (minArrivalTime < maxArrivalTime) && (minServiceTime < maxServiceTime) &&
                (minServiceTime > 0) && (minArrivalTime > 0) && numberOfClients > 0 && numberOfQueues > 0
                && numberOfQueues <= numberOfClients;
    }

    public List<Task> generateRandomTasks() {
        List<Task> generatedTasks = Collections.synchronizedList(new CustomArrayList());

        for (int i = 0; i < numberOfClients; i++) {
            Random random = new Random();
            Task t = new Task(i, random.nextInt(minArrivalTime, maxArrivalTime + 1), random.nextInt(minServiceTime, maxServiceTime + 1));
            generatedTasks.add(t);
        }
        generatedTasks.sort(Task::compareTo);
        return generatedTasks;
    }

    @Override
    public void run() {
        int currentTime = 0;

        Logger logger = Logger.getLogger("log");
        try {

            FileHandler fh = new FileHandler("log.txt");
            logger.addHandler(fh);
            CustomFormatter formatter = new CustomFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (currentTime < simulationInterval) {
            logger.info("Time " + currentTime);
            for (int i = 0; i < tasks.size() && i >= 0; i++) {
                if (tasks.get(i).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(tasks.get(i));
                    tasks.remove(i);
                    i--;
                }
            }
            int i = 0;
            for (Server s : scheduler.getServers()) {
                logger.info("Queue " + i + ": " + s);
                i++;
            }
            logger.info("\n");
            try {
            simulationFrame.getWaitingArea().setText(tasks.toString());
                simulationFrame.getQueue1Area().setText(scheduler.getServers().get(0).toString());
                simulationFrame.getQueue2Area().setText(scheduler.getServers().get(1).toString());
                simulationFrame.getQueue3Area().setText(scheduler.getServers().get(2).toString());
                simulationFrame.getQueue4Area().setText(scheduler.getServers().get(3).toString());
                simulationFrame.getQueue5Area().setText(scheduler.getServers().get(4).toString());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Queue is closed");
            }
            simulationFrame.getTimeField().setText(String.valueOf(currentTime));
            if (tasks.size() == 0 && scheduler.areServersEmpty()) {
                break;
            }
            currentTime++;
            synchronized (this) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        JOptionPane.showMessageDialog(null, "SIMULATION DONE AT: " + currentTime + " s", "DONE", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new SimulationManager();
    }
}
