import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderInladenPanel extends JPanel {
    private JPanel magazijn;
    private List<String[]> LijnLocaties;
    private String[] locaties = {"A1", "A2", "A3"};

    public OrderInladenPanel(int OrderID) {
        // Standaard waarden
        setPreferredSize(new Dimension(1500, 900));
        setLayout(null); // Use null layout for absolute positioning

        magazijn = new JPanel(new GridLayout(5, 5)); // Create a 5x5 grid layout
        magazijn.setSize(500, 500);
        magazijn.setLocation(100, 150); // Set the location of the grid panel

        // Coordinates with reverse order
        String[] rows = {"E", "D", "C", "B", "A"};
        for (int j = 5; j > 0; j--) {
            for (int i = 0; i < 5; i++) {
                String coord = rows[i] + j;
                JLabel cellLabel = new JLabel(coord, SwingConstants.CENTER);
                cellLabel.setPreferredSize(new Dimension(100, 100));
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                magazijn.add(cellLabel);
            }
        }
        add(magazijn); // Add the grid panel to the main panel
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED); // Set the color of the line
        g2d.setStroke(new BasicStroke(2)); // Set the thickness of the line
        g2d.drawLine(magazijn.getX(), magazijn.getY(), magazijn.getX() + magazijn.getWidth(), magazijn.getY() + magazijn.getHeight()); // Draw a line from the top left to the bottom right of the magazijn panel
    }
}
