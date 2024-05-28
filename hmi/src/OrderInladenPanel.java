import javax.swing.*;
import java.awt.*;

public class OrderInladenPanel extends JPanel {
    private JPanel magazijn;

    public OrderInladenPanel(int OrderID) {
            // Standaard waarden
        setPreferredSize(new Dimension(1500, 900));
        setLayout(null); // Use null layout for absolute positioning

        magazijn = new JPanel(new GridLayout(5, 5)); // Create a 5x5 grid layout
        magazijn.setSize(500, 500);
        magazijn.setLocation(100, 150); // Set the location of the grid panel

            // Coordinates with reverse order
        String[] rows = {"E", "D", "C", "B", "A"};
        for (int i = 0; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                String coord = rows[i] + j;
                JLabel cellLabel = new JLabel(coord, SwingConstants.CENTER);
                cellLabel.setPreferredSize(new Dimension(100, 100));
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                magazijn.add(cellLabel);
            }
        }
        add(magazijn); // Add the grid panel to the main panel



    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }


}