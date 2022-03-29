package mvc.controller;

import mvc.model.Server;
import mvc.model.Task;
import mvc.view.InputFrame;
import mvc.view.SimulationFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
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
    private Logger logger;

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
        return (minArrivalTime <= maxArrivalTime) && (minServiceTime <= maxServiceTime) &&
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

    public void setUpLogger() {
        logger = Logger.getLogger("log");
        try {
            FileHandler fh = new FileHandler("log.txt");
            logger.addHandler(fh);
            CustomFormatter formatter = new CustomFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSimulationFrame(int currentTime) {
        simulationFrame.getWaitingArea().setText(tasks.toString());
        if (tasks.size() == 0) {
            simulationFrame.getWaitingArea().setVisible(false);
        }
        int numberOfDisplayedQueues = Math.min(numberOfQueues, 5);
        for (int i = 0; i < numberOfDisplayedQueues; i++) {
            simulationFrame.getQueueAreas()[i].setText(scheduler.getServers().get(i).toString());
            if (scheduler.getServers().get(i).getTasksQueue().size() == 0) {
                simulationFrame.getQueueAreas()[i].setVisible(false);
                simulationFrame.getQueueLabels()[i].setForeground(Color.RED);
            } else {
                simulationFrame.getQueueAreas()[i].setVisible(true);
                simulationFrame.getQueueLabels()[i].setForeground(Color.GREEN);
            }
        }

        simulationFrame.getAvgWaitTimeField().setText(new DecimalFormat("0.00").format(scheduler.getAvgWaitingTime()));
        simulationFrame.getAvgServiceTimeField().setText(new DecimalFormat("0.00").format(scheduler.getAvgServiceTimePerQueues()));
        simulationFrame.getPeakHourField().setText(String.valueOf(scheduler.getPeakHour(currentTime)));
        simulationFrame.getTimeField().setText(String.valueOf(currentTime));
    }

    @Override
    public void run() {
        setUpLogger();
        int currentTime = 0;
        while (currentTime < simulationInterval) {
            logger.info("\nTime " + currentTime);
            for (int i = 0; i < tasks.size() && i >= 0; i++) {
                if (tasks.get(i).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(tasks.get(i));
                    tasks.remove(i);
                    i--;
                }
            }
            for (Server s : scheduler.getServers()) {
                logger.info("Queue " + (scheduler.getServers().indexOf(s) + 1) + ": " + s);
            }
            updateSimulationFrame(currentTime);
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
        JOptionPane.showMessageDialog(null, "SIMULATION DONE AT: " + currentTime + " s\n" + "Average waiting time: " + new DecimalFormat("0.00").format(scheduler.getFinalAvgWaitingTime() / (currentTime - 1)) + " s"
                + "\nAverage service time: " + new DecimalFormat("0.00").format(scheduler.getFinalAvgServiceTime() / (currentTime - 1)) + " s" + "\nPeak hour: " + scheduler.getPeakHour(currentTime) + " s", "DONE", JOptionPane.INFORMATION_MESSAGE);
        logger.info("\nAvg waiting time: " + new DecimalFormat("0.00").format(scheduler.getFinalAvgWaitingTime() / (currentTime - 1)) + " s" + "\nAvg service time: " +
                new DecimalFormat("0.00").format(scheduler.getFinalAvgServiceTime() / (currentTime - 1)) + " s" + "\nPeak Hour: " + scheduler.getPeakHour(currentTime) + " s");
    }

    public static void main(String[] args) {
        new SimulationManager();
    }
}
