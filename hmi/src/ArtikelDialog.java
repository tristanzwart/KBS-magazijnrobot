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

    private boolean isOk;

    public ArtikelDialog(JFrame frame, boolean modal) {
        super(frame, modal);

        //Standaard waarden
        setTitle("Artikel");
        setSize(300, 230);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        locatie = new JTextField();
        artikelnummer = new JTextField();
        voorraad = new JTextField();
        artikelnaam = new JTextField();

        JBok = new JButton("Ok");
        JBok.addActionListener(this);
        JBcancel = new JButton("annuleren");
        JBcancel.addActionListener(this);

        add(new JLabel("Locatie:"));
        add(locatie);
        add(new JLabel("Artikelnummer:"));
        add(artikelnummer);
        add(new JLabel("Voorraad:"));
        add(voorraad);
        add(new JLabel("Artikelnaam:"));
        add(artikelnaam);
        add(JBok);
        add(JBcancel);

        //setVisible(true);

    }

    public void toonDialog(){
        toonDialog("Artikel toevoegen", "", "", "", "");
    }

    public void toonDialog(String titel, String locatie, String artikelnummer, String voorraad, String artikelnaam){
        //TODO: Artikelnummer mag niet worden gemaakt door de gebruiker maar word gergeld door de db
        setTitle(titel);
        this.locatie.setText(locatie);
        this.artikelnummer.setText(artikelnummer);
        this.voorraad.setText(voorraad);
        this.artikelnaam.setText(artikelnaam);
        setVisible(true);
    }

    public String getLocatie (){
        return locatie.getText();
    }

    public String getArtikelnummer(){
        return artikelnummer.getText();
        //Moet eventueel een int returnen
    }

    public String getVooraad(){
        return voorraad.getText();
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
        setVisible(false);
    }
}


