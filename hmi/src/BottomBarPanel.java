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
    private JButton bekijken;
    private JButton inladen;

    private int HuidigeGeselecteerdeArtikel;
    private int HuidigeGeselecteerdeOrder;
    private int HuidigeVoorraad;

    public BottomBarPanel(GUI gui, ArtikelDialog artikelDialog){
        this.gui = gui;
        this.artikelDialog = artikelDialog;
        setPreferredSize(new Dimension(1680, 100));
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 37));

        //Elementen initialiseren
        artikelAanpassen = new JButton("Artikel aanpassen");
        artikelAanpassen.addActionListener(this);
        verversen = new JButton("Verversen");
        verversen.addActionListener(this);
        bekijken =new JButton("Bekijken");
        bekijken.addActionListener(this);
        bekijken.setVisible(false);
        inladen =new JButton("Inladen");
        inladen.addActionListener(this);
        inladen.setVisible(false);




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
        }else if(buttonName.equals("bekijken")){
            add(bekijken);

        } else if (buttonName.equals("inladen")) {
            add(inladen);
        } else {
            //Knop bestaat niet
            System.out.println("Error: Knop bestaat niet");
        }
        revalidate();
    }

    public void setArtikelAanpassenStatus (boolean status) {
        artikelAanpassen.setVisible(status);
    }
    public void setOrderAanpassenStatus (boolean status) {
        bekijken.setVisible(status);
        inladen.setVisible(status);
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
            artikelDialog.toonDialog("Artikel Aanpassen", String.valueOf(HuidigeVoorraad));
            if (artikelDialog.isOk()) {
                if (artikelDialog.getBeginVoorraad() != artikelDialog.getVooraad()) {
                    if (artikelDialog.getVooraad() == -99999999) {
                        System.out.println("ongeldige invoer");
                        gui.giveSideFeedback("Ongeldige invoer!");
                    } else {
                        Database.updateStockItem(HuidigeGeselecteerdeArtikel, artikelDialog.getVooraad());
                        System.out.println("Geupdate volgens de volgende parameters: " + HuidigeGeselecteerdeArtikel + artikelDialog.getVooraad());
                        gui.updateVoorraadTableData();
                    }
                }
            }
            
        } else if (e.getSource() == bekijken) {
            System.out.println("mooi");
            System.out.println(HuidigeGeselecteerdeOrder);
            OrderDialog orderdia = new OrderDialog(gui, true, HuidigeGeselecteerdeOrder);
            gui.updateOrderTabelData();
        }
        else if(e.getSource() == inladen){

            OrderInladenDialog orderin = new OrderInladenDialog(gui,true, HuidigeGeselecteerdeOrder, gui);
            Database.VoorraadVerlagen(HuidigeGeselecteerdeOrder);
        }
    }

    public void setHuidigeGeselecteerdeArtikel(int getHuidigeGeselecteerdeArtikel) {
        this.HuidigeGeselecteerdeArtikel = getHuidigeGeselecteerdeArtikel;
    }

    public void setHuidigeVoorraad(int huidigeVoorraad) {
        HuidigeVoorraad = huidigeVoorraad;
    }
    public void setHuidigeGeselecteerdeOrder(int getHuidigeGeselecteerdeOrder) {

        this.HuidigeGeselecteerdeOrder = getHuidigeGeselecteerdeOrder;
    }

    public int getHuidigeGeselecteerdeArtikel() {
        return HuidigeGeselecteerdeArtikel;
    }

    public int getHuidigeVoorraad() {
        return HuidigeVoorraad;
    }
}
