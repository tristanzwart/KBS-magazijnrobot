import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBarPanel extends JPanel implements ActionListener {
    private GUI gui;
    private JButton orders;
    private JButton voorraad;

    private JLabel feedback;

    public SideBarPanel(GUI gui){
        this.gui = gui;

        setPreferredSize(new Dimension(120, 1017));
        setLayout(new FlowLayout());

        orders = new JButton("Orders");
        orders.addActionListener(this);
        voorraad = new JButton("Voorraad");
        voorraad.addActionListener(this);
        feedback = new JLabel();

        Dimension buttonSize = new Dimension(90, 26);
        orders.setPreferredSize(buttonSize);
        voorraad.setPreferredSize(buttonSize);

        add(orders);
        add(voorraad);
        add(feedback);

        setVisible(true);
    }

    public void giveFeedback(String feedbackinput) {
        this.feedback.setText(feedbackinput);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == voorraad){
            //De vooraad knop is ingedrukt
            System.out.println("Vooraad scherm tonen....");
            //Toon het voorraadscherm
            gui.toonScherm("voorraad");
        }else if(e.getSource() == orders){
            //De vooraad knop is ingedrukt
            System.out.println("Orders scherm tonen....");
            //Toon het voorraadscherm
            gui.toonScherm("order");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
