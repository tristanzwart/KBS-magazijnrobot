import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OrderDialog extends JDialog {
    private JLabel ordernummer, klantnummer, aanmaakdatum;

    private JTable tabel;
    private Database database;

    public OrderDialog(JFrame frame, boolean modal, int OrderID){
        super(frame, modal);
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

        // Create JTable with order lines data
        tabel = new JTable(rec, header);
        JScrollPane scrollPane = new JScrollPane(tabel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}

