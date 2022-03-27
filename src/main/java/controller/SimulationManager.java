package controller;

import model.Server;
import model.Task;
import view.InputFrame;
import view.SimulationFrame;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    private FileWriter myWriter;
    boolean isClosed = false;

    public SimulationManager() {
        File f = new File("log.txt");
        try {
            f.delete();
            f.createNewFile();
            myWriter = new FileWriter("log.txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
                isClosed = true;


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
        List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());

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
//            simulationFrame.getWaitingArea().setText(tasks.toString());
//            simulationFrame.getQueue1Area().setText(scheduler.getServers().get(0).toString());
//            simulationFrame.getQueue2Area().setText(scheduler.getServers().get(1).toString());
//            simulationFrame.getQueue3Area().setText(scheduler.getServers().get(2).toString());
//            simulationFrame.getTextField1().setText(String.valueOf(currentTime));
            currentTime++;
            if (tasks.size() == 0 && scheduler.areServersEmpty()) {
                break;
            }
            synchronized (this) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getSimulationInterval() {
        return simulationInterval;
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
//        Thread t = new Thread(gen);
//        t.start();

    }
}
