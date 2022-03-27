package view;

import javax.swing.*;

public class SimulationFrame {
    private JTextArea queue1Area;
    private JTextArea queue2Area;
    private JTextArea queue3Area;
    private JTextArea waitingArea;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel panel1;

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

    public JTextField getTextField1() {
        return textField1;
    }

    public JTextField getTextField2() {
        return textField2;
    }

    public JTextField getTextField3() {
        return textField3;
    }

    public JTextField getTextField4() {
        return textField4;
    }

    public JPanel getPanel1() {
        return panel1;
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
