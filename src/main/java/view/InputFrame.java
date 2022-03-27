package view;

import javax.swing.*;

public class InputFrame {
    private JTextField nOfClientsField;
    private JTextField nOfQueuesField;
    private JTextField simIntervalField;
    private JTextField minArrivalTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minServiceTimeField;
    private JTextField maxServiceTimeField;
    private JButton validateInputButton;
    private JButton startSimButton;
    private JTextArea textArea1;
    private JPanel panel1;
    private JRadioButton shortestQueueStrategyRadioButton;
    private JRadioButton shortestTimeQueueStrategyRadioButton;

    public JTextField getnOfClientsField() {
        return nOfClientsField;
    }

    public JTextField getnOfQueuesField() {
        return nOfQueuesField;
    }

    public JTextField getSimIntervalField() {
        return simIntervalField;
    }

    public JTextField getMinArrivalTimeField() {
        return minArrivalTimeField;
    }

    public JTextField getMaxArrivalTimeField() {
        return maxArrivalTimeField;
    }

    public JTextField getMinServiceTimeField() {
        return minServiceTimeField;
    }

    public JTextField getMaxServiceTimeField() {
        return maxServiceTimeField;
    }

    public JButton getValidateInputButton() {
        return validateInputButton;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JRadioButton getShortestQueueStrategyRadioButton() {
        return shortestQueueStrategyRadioButton;
    }

    public JRadioButton getShortestTimeQueueStrategyRadioButton() {
        return shortestTimeQueueStrategyRadioButton;
    }

    public JButton getStartSimButton() {
        return startSimButton;
    }

    public JTextArea getTextArea1() {
        return textArea1;
    }

    private JFrame frame;

    public JFrame getFrame() {
        return frame;
    }

    public InputFrame() {
        frame = new JFrame("Input");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
