import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RobotPanel extends JPanel implements ActionListener {
    private GUI gui;
    private JButton[][] buttons = new JButton[5][5];
    private ArduinoCom arduino1;
    private ArduinoCom arduino2;

    public RobotPanel(GUI gui) {
        this.gui = gui;
        setPreferredSize(new Dimension(500, 500));
        setLayout(new GridLayout(5, 5));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new JButton("" + (char)('A' + j) + (i + 1));
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }
        arduino1 = gui.getArduino1();
        arduino2 = gui.getArduino2();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton sourceButton = (JButton) e.getSource();
        String buttonLabel = sourceButton.getText();
        char yas = buttonLabel.charAt(0);
        char xas = buttonLabel.charAt(1);

        String naarx = ArduinoCom.getCoordinates(yas);
        String naary = ArduinoCom.getCoordinates(xas);

        arduino1.verstuurData(naary);
        arduino2.verstuurData(naarx);

    }

    private void sluitPoorten() {
        arduino1.closePort();
        arduino2.closePort();
    }
}




