package view;

import javax.swing.*;

public class SimulationFrame {
    private JTextArea queue1Area;
    private JTextArea queue2Area;
    private JTextArea queue3Area;
    private JTextArea waitingArea;
    private JTextField timeField;
    private JTextField avgWaitTimeField;
    private JTextField avgServiceTimeField;
    private JTextField peakHourField;
    private JPanel panel1;
    private JTextArea queue4Area;
    private JTextArea queue5Area;
    private JButton closeSimButton;
    private JLabel queue1Label;
    private JLabel queue2Label;
    private JLabel queue3Label;
    private JLabel queue4Label;
    private JLabel queue5Label;

    public JTextArea[] getQueueAreas() {
        JTextArea[] areas = new JTextArea[5];
        areas[0] = queue1Area;
        areas[1] = queue2Area;
        areas[2] = queue3Area;
        areas[3] = queue4Area;
        areas[4] = queue5Area;
        return areas;
    }

    public JLabel[] getQueueLabels() {
        JLabel[] labels = new JLabel[5];
        labels[0] = queue1Label;
        labels[1] = queue2Label;
        labels[2] = queue3Label;
        labels[3] = queue4Label;
        labels[4] = queue5Label;
        return labels;
    }

    public JTextArea getQueue1Area() {
        return queue1Area;
    }

    public JTextArea getQueue2Area() {
        return queue2Area;
    }

    public JTextArea getQueue3Area() {
        return queue3Area;
    }

    public JTextArea getWaitingArea() {
        return waitingArea;
    }

    public JTextField getTimeField() {
        return timeField;
    }

    public JTextField getAvgWaitTimeField() {
        return avgWaitTimeField;
    }

    public JTextField getAvgServiceTimeField() {
        return avgServiceTimeField;
    }

    public JTextField getPeakHourField() {
        return peakHourField;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JTextArea getQueue4Area() {
        return queue4Area;
    }

    public JTextArea getQueue5Area() {
        return queue5Area;
    }

    public JButton getCloseSimButton() {
        return closeSimButton;
    }

    public JFrame getFrame() {
        return frame;
    }

    JFrame frame;

    public SimulationFrame() {
        frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
