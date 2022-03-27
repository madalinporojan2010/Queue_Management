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
