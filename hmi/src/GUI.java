import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;



public class GUI extends JFrame{
    private String huidigScherm;
    private MainPanel mainPanel;
    private SideBarPanel sideBar;
    private BottomBarPanel bottomBar;

    private ArtikelDialog artikelDialog;

    private Database db;

    private JTable table;
    private JScrollPane scrollPane;

    private ArduinoCom arduino1;
    private ArduinoCom arduino2;

    public GUI() {
        arduino1 = new ArduinoCom("COM3");
        arduino2 = new ArduinoCom("COM7");
        artikelDialog = new ArtikelDialog(this, true);
        db = new Database();

        //Init tablel met lege data niet toevoegen aan mainPanel
        Object[][] data = {{}};
        String[] columnNames = {};

        table = new JTable(new DefaultTableModel(data, columnNames));
        table.setDefaultEditor(Object.class, null);


        // Set selection mode
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Onderstaande zorgt ervoor dat de knop word getoond als er iets geselecteerd word
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1) {
                    bottomBar.setArtikelAanpassenStatus(true);
                    bottomBar.setOrderAanpassenStatus(true);
                } else {
                    bottomBar.setArtikelAanpassenStatus(false);
                    bottomBar.setOrderAanpassenStatus(false);
                }
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    TableModel model = table.getModel();

                    // Haal het artikelnummer op van de geselecteerde rij
                    if(huidigScherm.equals("voorraad")) {
                        bottomBar.setHuidigeGeselecteerdeArtikel(Integer.parseInt(model.getValueAt(selectedRow, 1).toString()));
                        bottomBar.setHuidigeVoorraad(Integer.parseInt(model.getValueAt(selectedRow, 2).toString()));
                    } else if (huidigScherm.equals("order")) {
                        bottomBar.setHuidigeGeselecteerdeOrder(Integer.parseInt(model.getValueAt(selectedRow, 0).toString()));
                    }



                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && huidigScherm.equals("voorraad")) {
                    // Haal de waarden op van de 2de en 3de kolom van de geselecteerde rij
                    Object valueInSecondColumn = table.getValueAt(row, 1); // Kolommen zijn 0-gebaseerd
                    Object valueInThirdColumn = table.getValueAt(row, 2); // Kolommen zijn 0-gebaseerd
                    bottomBar.setHuidigeVoorraad(Integer.parseInt(valueInThirdColumn.toString()));
                    bottomBar.setHuidigeGeselecteerdeArtikel(Integer.parseInt(valueInSecondColumn.toString()));

                    // Uw code om de dialoog te openen gaat hier
                    //TODO: Call modal dialoge
                    artikelDialog.toonDialog("Artikel Aanpassen", String.valueOf(bottomBar.getHuidigeVoorraad()));
                    //TODO:Get data from dialoge
                    if (artikelDialog.isOk()) {
                        if (artikelDialog.getBeginVoorraad() != artikelDialog.getVooraad()) {
                            if (artikelDialog.getVooraad() == -99999999) {
                                System.out.println("ongeldige invoer");
                                giveSideFeedback("Ongeldige invoer!");
                            } else {
                                Database.updateStockItem(bottomBar.getHuidigeGeselecteerdeArtikel(), artikelDialog.getVooraad());
                                System.out.println("Geupdate volgens de volgende parameters: " + bottomBar.getHuidigeGeselecteerdeArtikel() + artikelDialog.getVooraad());
                                updateVoorraadTableData();
                            }
                        }
                    }
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (arduino1 != null) {
                    arduino1.closePort();
                }
                if (arduino2 != null) {
                    arduino2.closePort();
                }
                System.out.println("Comm poorten gesloten!");
            }
        });



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
        bottomBar = new BottomBarPanel(this, artikelDialog);
        add(bottomBar, BorderLayout.SOUTH);
        toonScherm("robot");

        setVisible(true);


    }

    public void toonScherm(String schermNaam){
        int schermNummer;
        if(schermNaam.equals("voorraad")) {
            huidigScherm = "voorraad";
            schermNummer = 1;
        } else if (schermNaam.equals("order")) {
            huidigScherm = "order";
            schermNummer = 2;
        }else if(schermNaam.equals("robot")) {
            huidigScherm = "robot";
            schermNummer = 3;
        }else {
            System.out.println("Dit scherm bestaat niet: " + schermNaam);
            return;
        }

        //Deze functie is voor het tonen van het voorraadscherm
        //Deze functie wordt aangeroepen vanuit de SideBarPanel als je op de knop drukt
        //Er is dus een functie voor elk apart scherm buiten de dialoogen

        //Leeg het Jpanel
        mainPanel.removeAll();

        //Update de tabel data
        if(schermNummer == 1) {
            updateVoorraadTableData();
        } else if (schermNummer == 2) {
            updateOrderTabelData();
        }



        // Add scroll pane to main panel
        if (schermNummer == 1 || schermNummer == 2) {
            mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0 ,0));
            // Update table data for "voorraad" or "order" screens
            if (schermNummer == 1) {
                updateVoorraadTableData();
            } else if (schermNummer == 2) {
                updateOrderTabelData();
            }
            // Add scroll pane with table to main panel
            mainPanel.add(scrollPane);
        } else if (schermNummer == 3) {

            // Add content for "robot" screen
            RobotPanel robotPanel = new RobotPanel(this);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(210, 250, 0 ,0));
            mainPanel.add(robotPanel);
        }

        //Revalidate en repaint
        mainPanel.revalidate();
        mainPanel.repaint();

        bottomBar.removeAll();
        if(schermNummer == 1) {
//            bottomBar.addButton("Artikel toevoegen");
            bottomBar.addButton("Artikel aanpassen");
            bottomBar.addButton("Verversen");

        } else if (schermNummer == 2) {
            bottomBar.addButton("Verversen");
            bottomBar.addButton("bekijken");
        }

        bottomBar.revalidate();
    }

    public void giveSideFeedback(String feedback) {
        sideBar.giveFeedback(feedback);
    }


    public void updateVoorraadTableData() {
    Object[][] data = db.getStockItems();

    String[] columnNames = {"Locatie", "Artikelnummer", "Op vooraad", "Artikelnaam"};
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setDataVector(data, columnNames);
    giveSideFeedback("");
}



public void updateOrderTabelData() {
    Object[][] data = db.getOrders();

    String[] columnNames = {"Ordernummer", "Klantennummer", "Aantal producten", "Opmerkingen"};
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setDataVector(data, columnNames);
    giveSideFeedback("");
}

    public String getHuidigScherm(){
        return huidigScherm;
    }

    public ArduinoCom getArduino1() {
        return arduino1;
    }

    public ArduinoCom getArduino2() {
        return arduino2;
    }



}


