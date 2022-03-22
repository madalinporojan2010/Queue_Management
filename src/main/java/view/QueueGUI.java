package view;

import javax.swing.*;

public class QueueGUI {
    private JTextField nOfClientsField;
    private JTextField nOfQueuesField;
    private JTextField simIntervalField;
    private JTextField minArrivalTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minServiceTimeField;
    private JTextField maxServiceTimeFIeld;
    private JButton validateInputButton;
    private JButton startSimButton;
    private JTextArea textArea1;
    private JPanel panel1;

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

    public JTextField getMaxServiceTimeFIeld() {
        return maxServiceTimeFIeld;
    }

    public JButton getValidateInputButton() {
        return validateInputButton;
    }

    public JButton getStartSimButton() {
        return startSimButton;
    }

    public JTextArea getTextArea1() {
        return textArea1;
    }

    public QueueGUI(JFrame frame) {
        frame = new JFrame("UTCN Assignment #1");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
