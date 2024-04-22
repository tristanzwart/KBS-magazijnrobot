import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{
    private MainPanel mainPanel;
    private SideBarPanel sideBar;
    private BottomBarPanel bottomBar;

    private Database db;

    private JTable table;

    public GUI() {
        db = new Database();
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
        //Deze functie is voor het tonen van het voorraadscherm
        //Deze functie wordt aangeroepen vanuit de SideBarPanel als je op de knop drukt
        //Er is dus een functie voor elk apart scherm buiten de dialoogen

        //Leeg het Jpanel
        mainPanel.removeAll();

        //TODO: Voeg de elementen weer toe
//        JLabel label = new JLabel("Voorraad scherm");
//        mainPanel.add(label);

        //Create column names
        String[] columnNames = {"Locatie", "Artikelnummer", "Op vooraad", "Artikelnaam"};

        /*
        // Deze code was voor test doeleinden / dummy data
        Object[][] data = {
                {"A1", "1133045", "20", "Zonnebril"},
                {"D3", "1133345", "20", "Mok"},
                {"C5", "1033345", "20", "Zaklamp"},
                {"A2", "1133305", "20", "T-shirt"}

        };
        */

        //Haal de data op uit de database
        Object[][] data = db.getStockItems();

        // Create table with data
        table = new JTable(new DefaultTableModel(data, columnNames));

        // Set selection mode
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create scroll pane and add table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1800, 917));

        // Add scroll pane to main panel
        mainPanel.add(scrollPane);

        //TODO: Maak een extra panel voor de bottom bar met knoppen

        //Revalidate en repaint
        mainPanel.revalidate();
        //mainPanel.repaint();

        bottomBar.removeAll();
        bottomBar.addButton("Artikel toevoegen");
        bottomBar.addButton("Artikel aanpassen");
        bottomBar.addButton("Verversen");
    }

    public void updateTableData() {
    Object[][] data = db.getStockItems();

    String[] columnNames = {"Locatie", "Artikelnummer", "Op vooraad", "Artikelnaam"};
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setDataVector(data, columnNames);
}

//Naam: Kaas, nummer: 224575, voorraad: 20
//{"kaas", "224575", "20"},

    //Loopen door de buitenste array
    //In de binnense array moet je voor plek 0 data toevoegen
}
