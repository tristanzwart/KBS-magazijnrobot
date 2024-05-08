import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BottomBarPanel extends JPanel implements ActionListener {
    private GUI gui;

    private ArtikelDialog artikelDialog;

    private JButton artikelToevoegen;
    private JButton artikelAanpassen;
    private JButton verversen;

    private int HuidigeGeselecteerdeArtikel;
    private int HuidigeVoorraad;

    public BottomBarPanel(GUI gui, ArtikelDialog artikelDialog){
        this.gui = gui;
        this.artikelDialog = artikelDialog;
        setPreferredSize(new Dimension(1680, 100));
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 37));

        //Elementen initialiseren
//        artikelToevoegen = new JButton("Artikel toevoegen");
//        artikelToevoegen.addActionListener(this);
        artikelAanpassen = new JButton("Artikel aanpassen");
        artikelAanpassen.addActionListener(this);
        verversen = new JButton("Verversen");
        verversen.addActionListener(this);



        setVisible(true);
    }

    public void addButton(String buttonName){
        if(buttonName.isEmpty()){
            //Knop naam is leeg
            System.out.println("Error: Knop naam is leeg");
        }else if(buttonName.equals("Artikel toevoegen")){
            add(artikelToevoegen);
        } else if (buttonName.equals("Artikel aanpassen")) {
            artikelAanpassen.setVisible(false);
            add(artikelAanpassen);
        } else if (buttonName.equals("Verversen")) {
            add(verversen);
        }else {
            //Knop bestaat niet
            System.out.println("Error: Knop bestaat niet");
        }
        revalidate();
    }

    public void setArtikelAanpassenStatus (boolean status) {
        artikelAanpassen.setVisible(status);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == verversen){
            System.out.println("Verversen");
            if(gui.getHuidigScherm().equals("voorraad")) {
                //Ververs de voorraad tabel
                gui.updateVoorraadTableData();
            }else if(gui.getHuidigScherm().equals("order")){
                //Ververs de orders tabel
                gui.updateOrderTabelData();
            }

        } else if (e.getSource() == artikelAanpassen) {
            //TODO: Call modal dialoge
            artikelDialog.toonDialog("Artikel Aanpassen", String.valueOf(HuidigeVoorraad));
            //TODO:Get data from dialoge
            if (artikelDialog.isOk()) {
                if (artikelDialog.getBeginVoorraad() != artikelDialog.getVooraad()) {
                    if (artikelDialog.getVooraad() == -99999999) {
                        System.out.println("ongeldige invoer");
                    } else {
                        Database.updateStockItem(HuidigeGeselecteerdeArtikel, artikelDialog.getVooraad());
                        System.out.println("updated using following parameters: " + HuidigeGeselecteerdeArtikel + artikelDialog.getVooraad());
                        gui.updateVoorraadTableData();
                    }
                }
            }
            //TODO: Add data to database

            //TODO: Refresh table
            
        }
    }

    public void setHuidigeGeselecteerdeArtikel(int getHuidigeGeselecteerdeArtikel) {
        this.HuidigeGeselecteerdeArtikel = getHuidigeGeselecteerdeArtikel;
    }

    public void setHuidigeVoorraad(int huidigeVoorraad) {
        HuidigeVoorraad = huidigeVoorraad;
    }
}
