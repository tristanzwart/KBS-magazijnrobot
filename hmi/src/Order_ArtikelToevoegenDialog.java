import javax.swing.*;
import java.awt.*;

public class Order_ArtikelToevoegenDialog extends JDialog {
    private JTextField artikelnummerField;
    private JTextField hoeveelheidField;

    private int OrderID;
    private int hoeveelheid;
    private int artikelnummer;
    private String feedback;
    private Bevestigingsdialog bevestigingsdialog = new Bevestigingsdialog();

    public Order_ArtikelToevoegenDialog(int OrderID) {
        this.OrderID = OrderID;
        setTitle("Artikel Toevoegen");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Artikelnummer:"));
        artikelnummerField = new JTextField();
        panel.add(artikelnummerField);

        panel.add(new JLabel("Hoeveelheid:"));
        hoeveelheidField = new JTextField();
        panel.add(hoeveelheidField);

        JButton confirmButton = new JButton("Toevoegen");
        confirmButton.addActionListener(e -> {
            try {
                this.hoeveelheid = Integer.parseInt(hoeveelheidField.getText());
                this.artikelnummer = Integer.parseInt(artikelnummerField.getText());
                String Artikelnaam = Database.getStockItemName(this.artikelnummer);
                if(Artikelnaam != null) {
                    bevestigingsdialog.show("Weet u zeker dat u " + hoeveelheid + " keer " + Artikelnaam + " wilt toevoegen?");
                    if (bevestigingsdialog.antwoord() == true) {
                        Database.addOrderLine(OrderID, hoeveelheid, artikelnummer);
                        this.feedback = "Artikel toegevoegd!";
                    } else if (bevestigingsdialog.antwoord() == false) {
                        this.feedback = "";
                    }

                    setVisible(false);
                } else {
                    this.feedback = "Artikelnummer bestaat niet";
                    setVisible(false);
                }

            } catch (NumberFormatException ex) {
                this.feedback = "Ongeldige invoer bij toevoegen van Artikel";
                setVisible(false);
            }


        });
        panel.add(confirmButton);

        JButton cancelButton = new JButton("Annuleren");
        cancelButton.addActionListener(e -> {
            // Handle cancel action
            setVisible(false);
        });
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }

    public String getFeedback() {
        return feedback;
    }
}
