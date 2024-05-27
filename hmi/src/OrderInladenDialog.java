import javax.swing.*;
import java.awt.*;

public class OrderInladenDialog extends JDialog {
    private JPanel magazijn;

    public OrderInladenDialog(JFrame frame, boolean modal) {
        super(frame, modal);

        // Standaard waarden
        setTitle("Order Picken");
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());

        magazijn = new JPanel(new GridLayout(5, 5)); // Create a 5x5 grid layout
        magazijn.setPreferredSize(new Dimension(500, 500));
        String[] rows = {"E", "D", "C", "B", "A"};
        for (int i = 0; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                String coord = rows[i] + (j);
                JLabel cellLabel = new JLabel(coord, SwingConstants.CENTER);
                cellLabel.setPreferredSize(new Dimension(100, 100));
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                magazijn.add(cellLabel);
            }
        }

        add(magazijn); // Add the panel to the dialog
    }
}