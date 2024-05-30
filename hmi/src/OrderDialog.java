import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OrderDialog extends JDialog implements ActionListener {
    private JLabel ordernummer, klantnummer, aanmaakdatum;

    private JTable tabel;
    private Database database;

    private JButton saveButton;
    private JButton deleteButton;
    private JButton addButton;
    private JLabel feedbackLabel;

    private int OrderID;
    private Order_ArtikelToevoegenDialog ArtikeltoevoegenDialog;
    private Bevestigingsdialog bevestigingsdialog = new Bevestigingsdialog();


    public OrderDialog(JFrame frame, boolean modal, int OrderID){
        super(frame, modal);
        this.OrderID = OrderID;
        database = new Database();
        String[] info = database.getorderinfo(OrderID);

        setTitle("Order "+ OrderID);
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        if (info != null) {
            JPanel infopanel = new JPanel();
            infopanel.setLayout(new GridLayout(1, 3, 0, 10)); // 3 rows, 1 column, vertical gap of 10 pixels

            ordernummer = new JLabel("Ordernummer: " + OrderID);
            ordernummer.setBorder(new CompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10), // EmptyBorder to add margin
                    BorderFactory.createLineBorder(Color.BLACK, 1) // LineBorder with black color and thickness 1
            ));
            ordernummer.setHorizontalAlignment(JLabel.CENTER);
            infopanel.add(ordernummer);

            klantnummer = new JLabel("Klantnummer: " + info[1]);
            klantnummer.setBorder(new CompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10), // EmptyBorder to add margin
                    BorderFactory.createLineBorder(Color.BLACK, 1) // LineBorder with black color and thickness 1
            ));
            klantnummer.setHorizontalAlignment(JLabel.CENTER);
            infopanel.add(klantnummer);

            aanmaakdatum = new JLabel("Aanmaakdatum: " + info[2]);
            aanmaakdatum.setBorder(new CompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10), // EmptyBorder to add margin
                    BorderFactory.createLineBorder(Color.BLACK, 1) // LineBorder with black color and thickness 1
            ));
            aanmaakdatum.setHorizontalAlignment(JLabel.CENTER);

            infopanel.add(aanmaakdatum);

            // Add infopanel to the center of the dialog
            add(infopanel, BorderLayout.NORTH);
        } else {
            JLabel errorLabel = new JLabel("Order not found for OrderID: " + OrderID);
            errorLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Adding margin around error label
            add(errorLabel, BorderLayout.NORTH);
        }

        Object[][] rec = database.getOrderlines(OrderID);
        String[] header = { "Orderregel" ,"Artikelnummer", "Artikelbeschrijving", "Hoeveelheid" };


        // Custom Tabel maken
        DefaultTableModel model = new DefaultTableModel(rec, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Alleen de hoeveelheid kolom mag aangepast worden
                return column == 3;
            }
        };

        // JTable aanmaken met Eigen model hierboven gemaakt
        tabel = new JTable(model);
        tabel.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tabel);
        add(scrollPane, BorderLayout.CENTER);
        // zorgt ervoor dat de verwijder knop zichtbaar word.
        tabel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // If a row is selected, make the "Artikel verwijderen" button visible
                // Otherwise, make it invisible
                deleteButton.setVisible(!tabel.getSelectionModel().isSelectionEmpty());
            }
        });

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Hoeveelheden Opslaan");
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);


        addButton = new JButton("Artikel Toevoegen");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        deleteButton = new JButton("Artikel verwijderen");
        deleteButton.addActionListener(this);
        deleteButton.setVisible(false);
        buttonPanel.add(deleteButton);

        feedbackLabel = new JLabel("");
        buttonPanel.add(feedbackLabel);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == saveButton){
            if (tabel.isEditing()) {
                tabel.getCellEditor().stopCellEditing();
            }
            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            int numrows = model.getRowCount();
            boolean allRowsValid = true;

            // Eerst controleren of alle rijen geldige waarden hebben
            for(int i = 0; i < numrows; i++){
                try {
                    Integer.parseInt(model.getValueAt(i, 0).toString());
                    Integer.parseInt(model.getValueAt(i, 3).toString());
                } catch (NumberFormatException ex) {
                    allRowsValid = false;
                    System.out.println("Error: Cell waarde in regel " + (i+1) + " is geen integer.");
                    break;
                }
            }

            // Als alle rijen geldige waarden hebben, dan updaten we de database
            if(allRowsValid){
                for(int i = 0; i < numrows; i++){
                    int orderLineID = Integer.parseInt(model.getValueAt(i, 0).toString());
                    int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
                    if (quantity <= 0) {
                        if (numrows != 1) {
                            Database.deleteOrderLine(orderLineID);
                        } else {
                            JOptionPane.showMessageDialog(null, "U kunt niet alle artikelen uit een order verwijderen! Aantal van laatste artikel veranderd naar 1.", "Waarschuwing", JOptionPane.WARNING_MESSAGE);
                            Database.updateOrderLine(this.OrderID, 1, orderLineID);
                        }

                    } else {
                        Database.updateOrderLine(this.OrderID, quantity, orderLineID);
                    }
                }
                setVisible(false);
            } else {
                feedbackLabel.setText("Niet alle regels zijn geldig");
            }


        } else if (e.getSource() == addButton){
            feedbackLabel.setText("");
            ArtikeltoevoegenDialog = new Order_ArtikelToevoegenDialog(OrderID);
            feedbackLabel.setText(ArtikeltoevoegenDialog.getFeedback());

            //ververs tabel
            Object[][] rec = database.getOrderlines(OrderID);
            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            model.setDataVector(rec, new String[] { "Orderregel" ,"Artikelnummer", "Artikelbeschrijving", "Hoeveelheid" });
        } else if (e.getSource() == deleteButton){
            int selectedRow = tabel.getSelectedRow();
            if(tabel.getModel().getRowCount() <= 1) {
                feedbackLabel.setText("U kunt niet alle artikelen verwijderen!");
            } else {
                if (selectedRow != -1) { // -1 means no row is selected
                    int orderLineID = Integer.parseInt(tabel.getValueAt(selectedRow, 0).toString());
                    bevestigingsdialog.show("Weet u zeker dat u deze regel wil verwijderen?");
                    if (bevestigingsdialog.antwoord()) {
                        Database.deleteOrderLine(orderLineID);
                        feedbackLabel.setText("Artikel verwijderd");
                    }

                }
            }

            //ververs tabel
            Object[][] rec = database.getOrderlines(OrderID);
            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            model.setDataVector(rec, new String[] { "Orderregel" ,"Artikelnummer", "Artikelbeschrijving", "Hoeveelheid" });
        }
    }






}

