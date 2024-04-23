import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUI extends JFrame{
    static String huidigScherm;
    private MainPanel mainPanel;
    private SideBarPanel sideBar;
    private BottomBarPanel bottomBar;

    private ArtikelToevoegenDialog artikelToevoegenDialog;

    private Database db;

    private JTable table;
    private JScrollPane scrollPane;

    public GUI() {
        artikelToevoegenDialog = new ArtikelToevoegenDialog(this, true);
        db = new Database();

        //Init tablel met lege data niet toevoegen aan mainPanel
        Object[][] data = {{}};
        String[] columnNames = {};

        table = new JTable(new DefaultTableModel(data, columnNames));

        // Set selection mode
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create scroll pane and add table to it
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1800, 917));

        //Standaard parameters
        setTitle("Magazijnrobot");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));
        //setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        //Einde standaard parameters

        //Stel het scherm in op fullscreen
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Voeg het Jpanel toe
        mainPanel = new MainPanel();
        add(mainPanel, BorderLayout.CENTER);
        sideBar = new SideBarPanel(this);
        add(sideBar, BorderLayout.EAST);
        bottomBar = new BottomBarPanel(this);
        add(bottomBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void toonVoorraadScherm(){
        GUI.huidigScherm = "voorraad";
        //Deze functie is voor het tonen van het voorraadscherm
        //Deze functie wordt aangeroepen vanuit de SideBarPanel als je op de knop drukt
        //Er is dus een functie voor elk apart scherm buiten de dialoogen

        //Leeg het Jpanel
        mainPanel.removeAll();

        //Update de tabel data
        updateVoorraadTableData();

        // Add scroll pane to main panel
        mainPanel.add(scrollPane);



        //Revalidate en repaint
        mainPanel.revalidate();
        //mainPanel.repaint();

        bottomBar.removeAll();
        bottomBar.addButton("Artikel toevoegen");
        bottomBar.addButton("Artikel aanpassen");
        bottomBar.addButton("Verversen");
    }

    public void toonOrdersScherm(){
        GUI.huidigScherm = "order";
        //Deze functie is voor het tonen van het voorraadscherm
        //Deze functie wordt aangeroepen vanuit de SideBarPanel als je op de knop drukt
        //Er is dus een functie voor elk apart scherm buiten de dialoogen

        //Leeg het Jpanel
        mainPanel.removeAll();

        //Update de tabel data
        updateOrderTabelData();

        // Add scroll pane to main panel
        mainPanel.add(scrollPane);



        //Revalidate en repaint
        mainPanel.revalidate();
        //mainPanel.repaint();

        bottomBar.removeAll();
        //TODO: Maak de knoppen voor de orders in de bottom bar
        bottomBar.addButton("Verversen");
    }

    public void updateVoorraadTableData() {
    Object[][] data = db.getStockItems();

    String[] columnNames = {"Locatie", "Artikelnummer", "Op vooraad", "Artikelnaam"};
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setDataVector(data, columnNames);
}



public void updateOrderTabelData() {
    Object[][] data = db.getOrders();

    String[] columnNames = {"Ordernummer", "Klantennummer", "Aantal producten", "Opmerkingen"};
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setDataVector(data, columnNames);
}

public void toonArtikelToevoegenDialog(){
        artikelToevoegenDialog.toonDialog();
    }

//Naam: Kaas, nummer: 224575, voorraad: 20
//{"kaas", "224575", "20"},

    //Loopen door de buitenste array
    //In de binnense array moet je voor plek 0 data toevoegen
}
