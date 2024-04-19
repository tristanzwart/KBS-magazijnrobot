import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{
    private MainPanel mainPanel;
    private SideBarPanel sideBar;

    public GUI() {
        //Standaard parameters
        setTitle("Magazijnrobot");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        //Einde standaard parameters

        //Stel het scherm in op fullscreen
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Voeg het Jpanel toe
        mainPanel = new MainPanel();
        add(mainPanel);
        sideBar = new SideBarPanel(this);
        add(sideBar);


        setVisible(true);
    }

    public void toonVoorraadScherm(){
        //Leeg het main panel zodat er nieuwe elementen kunnen worden toegevoegd

        //Leeg het Jpanel
        mainPanel.removeAll();

        //TODO: Voeg de elementen weer toe
        JLabel label = new JLabel("Voorraad scherm");
        mainPanel.add(label);

        //Revalidate en repaint
        mainPanel.revalidate();
        //mainPanel.repaint();
    }


}
