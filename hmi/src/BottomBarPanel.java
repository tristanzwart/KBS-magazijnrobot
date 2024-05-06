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

    public BottomBarPanel(GUI gui, ArtikelDialog artikelDialog){
        this.gui = gui;
        this.artikelDialog = artikelDialog;
        setPreferredSize(new Dimension(1680, 100));
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 37));

        //Elementen initialiseren
        artikelToevoegen = new JButton("Artikel toevoegen");
        artikelToevoegen.addActionListener(this);
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
            add(artikelAanpassen);
        } else if (buttonName.equals("Verversen")) {
            add(verversen);
        }else {
            //Knop bestaat niet
            System.out.println("Error: Knop bestaat niet");
        }
        revalidate();
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

        } else if (e.getSource() == artikelToevoegen) {
            //TODO: Call modal dialoge
            artikelDialog.toonDialog();
            //TODO:Get data from dialoge

            //TODO: Add data to database

            //TODO: Refresh table
            
        }
    }
}