import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBarPanel extends JPanel implements ActionListener {
    JButton voorraad;

    public SideBarPanel(){
        setPreferredSize(new Dimension(120, 1080));

        voorraad = new JButton("Voorraad");
        voorraad.addActionListener(this);
        add(voorraad);

        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == voorraad){
            //De vooraad knop is ingedrukt
            //TODO: Leeg het Jpanel
            //TODO: Voeg de elementen weer toe
            //TODO: Update de layout met revalidate
            //TODO: Eventueel nog repainten
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
