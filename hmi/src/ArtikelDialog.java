import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArtikelDialog extends JDialog implements ActionListener{
    private JTextField locatie;
    private JTextField artikelnummer;
    private JTextField voorraad;
    private JTextField artikelnaam;

    //"Locatie", "Artikelnummer", "Op vooraad", "Artikelnaam"

    private JButton JBok;
    private JButton JBcancel;

    private int beginVoorraad;

    private boolean isOk;

    public ArtikelDialog(JFrame frame, boolean modal) {
        super(frame, modal);

        //Standaard waarden
        setTitle("Artikel");
        setSize(300, 230);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        voorraad = new JTextField();

        JBok = new JButton("Ok");
        JBok.addActionListener(this);
        JBcancel = new JButton("annuleren");
        JBcancel.addActionListener(this);


        add(new JLabel("Voorraad:"));
        add(voorraad);

        add(JBok);
        add(JBcancel);



    }

    public void toonDialog(String titel, String voorraad){

        setTitle(titel);

        this.voorraad.setText(voorraad);
        this.beginVoorraad = Integer.parseInt(voorraad);

        setVisible(true);
    }



    public int getVooraad(){
        try {
            return Integer.parseInt(voorraad.getText());
        } catch (NumberFormatException e) {
            return -99999999;
        }

        //Moet eventueel een int returnen
    }

    public boolean isOk() {
        //Geeft terug of de actie is bevestigd
        return isOk;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == JBok){
            this.isOk = true;

        }
        if(e.getSource() == JBcancel){
            this.isOk = false;
        }
        setVisible(false);
    }


    public int getBeginVoorraad() {
        return beginVoorraad;
    }
}


