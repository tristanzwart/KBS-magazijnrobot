import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBarPanel extends JPanel implements ActionListener {
    private GUI gui;
    private JButton orders;
    private JButton voorraad;
    private JButton robot;

    private JLabel feedback;

    private JButton noodstop;

    public SideBarPanel(GUI gui){
        this.gui = gui;

        setPreferredSize(new Dimension(120, 1017));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));



        orders = new JButton("Orders");
        orders.addActionListener(this);
        voorraad = new JButton("Voorraad");
        voorraad.addActionListener(this);
        robot = new JButton("Robot");
        robot.addActionListener(this);
        noodstop = new JButton("Noodstop");
        noodstop.addActionListener(this);

        feedback = new JLabel();

        Dimension buttonSize = new Dimension(90, 26);
        orders.setPreferredSize(buttonSize);
        orders.setMaximumSize(buttonSize);
        voorraad.setPreferredSize(buttonSize);
        voorraad.setMaximumSize(buttonSize);
        robot.setPreferredSize(buttonSize);
        robot.setMaximumSize(buttonSize);
        noodstop.setPreferredSize(buttonSize);

        add(orders);
        add(Box.createRigidArea(new Dimension(0, 10))); // Adding vertical space between buttons
        add(voorraad);
        add(Box.createRigidArea(new Dimension(0, 10))); // Adding vertical space between buttons
        add(robot);
        add(Box.createRigidArea(new Dimension(0, 10))); // Adding vertical space between buttons
        add(feedback);

        noodstop.setBackground(Color.red);
        add(noodstop);


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
        }else if(e.getSource() == robot){
            System.out.println("Robot scherm tonen");
            gui.toonScherm("robot");
        }else if(e.getSource() == noodstop) {
            if (noodstop.getBackground().equals(Color.red)) {

            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
