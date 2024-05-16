import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBarPanel extends JPanel implements ActionListener {
    private GUI gui;
    private JButton orders;
    private JButton voorraad;

    private JLabel feedback;

    private JButton noodstop;

    public SideBarPanel(GUI gui){
        this.gui = gui;

        setPreferredSize(new Dimension(120, 1017));
        setLayout(new FlowLayout());

        orders = new JButton("Orders");
        orders.addActionListener(this);
        voorraad = new JButton("Voorraad");
        voorraad.addActionListener(this);
        noodstop = new JButton("Noodstop");
        noodstop.addActionListener(this);

        feedback = new JLabel();

        Dimension buttonSize = new Dimension(90, 26);
        orders.setPreferredSize(buttonSize);
        voorraad.setPreferredSize(buttonSize);
        noodstop.setPreferredSize(buttonSize);

        add(orders);
        add(voorraad);
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
        }else if(e.getSource() == noodstop) {
            if (noodstop.getBackground().equals(Color.GREEN)) {
                // code voor het stoppen van de noodstop
                noodstop.setBackground(Color.red);
            } else {
                // code voor het starten van de noodstop
                noodstop.setBackground(Color.GREEN);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
