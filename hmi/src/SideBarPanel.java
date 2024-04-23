import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBarPanel extends JPanel implements ActionListener {
    private GUI gui;
    private JButton orders;
    private JButton voorraad;

    public SideBarPanel(GUI gui){
        this.gui = gui;

        setPreferredSize(new Dimension(120, 1017));

        orders = new JButton("Orders");
        orders.addActionListener(this);
        voorraad = new JButton("Voorraad");
        voorraad.addActionListener(this);


        add(orders);
        add(voorraad);

        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == voorraad){
            //De vooraad knop is ingedrukt
            System.out.println("Vooraad scherm tonen....");
            //Toon het voorraadscherm
            gui.toonVoorraadScherm();
        }else if(e.getSource() == orders){
            //De vooraad knop is ingedrukt
            System.out.println("Orders scherm tonen....");
            //Toon het voorraadscherm
            gui.toonOrdersScherm();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
