import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class OrderInladenPanel extends JPanel {
    private JPanel magazijn;
    private List<String[]> LijnLocaties;
    private String[] locaties = {"A1", "B2", "E4", "C5"};
    private String status = "Pakken";
    private String lastLocation = "B2";

    //Tabel voor de dozen
    private ArrayList<JTable> doosTabellen;

    public OrderInladenPanel(List<Bin> bin) {
        doosTabellen = new ArrayList<>();
        Database db = new Database();
            // Standaard waarden
        setPreferredSize(new Dimension(1500, 900));
        setLayout(null); // Use null layout for absolute positioning

        magazijn = new JPanel(new GridLayout(5, 5)); // Create a 5x5 grid layout
        magazijn.setSize(500, 500);
        magazijn.setLocation(100, 150); // Set the location of the grid panel
        magazijn.setOpaque(false); // Make the panel transparent

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

        //Visualisatie van de dozen
        //Voor elke doos deze for loop uitvoeren
        for(int i = 0; i < bin.size(); i++) {
            JLabel doosLabel = new JLabel("Doos " + (i+1));
            doosLabel.setBounds(1100, 30 + i * 150, 100, 50);
            add(doosLabel);

            // Initialize the orderLineInfo list outside the loop
            List<Object[]> orderLineInfo = new ArrayList<>();

            // Iterate over all items in the bin
            for (List<Integer> item : bin.get(i).getItems()) {
                // Retrieve the orderId from the item
                int orderId = item.get(0);

                // Call the getOrderLineInfo method from the database with the orderId
                String[] orderLineInfoArray = db.getOrderLineInfo(orderId);

                // Add the entire orderLineInfoArray as a single array to the orderLineInfo list
                orderLineInfo.add(orderLineInfoArray);
            }

            // Convert List<Object[]> to Object[][]
            Object[][] data = new Object[orderLineInfo.size()][];
            for (int j = 0; j < orderLineInfo.size(); j++) {
                data[j] = orderLineInfo.get(j);
            }

            String[] columnNames = {"Artikelnummer", "Artikelnaam", "Op vooraad"};

            JTable doosTabel = new JTable(new DefaultTableModel(data, columnNames));
            doosTabel.setDefaultEditor(Object.class, null);
            doosTabel.getTableHeader().setReorderingAllowed(false);
            JScrollPane scrollPane = new JScrollPane(doosTabel);
            scrollPane.setBounds(885, 80 + i * 150, 500, 100);
            add(scrollPane);
            doosTabellen.add(doosTabel);
        }



    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLines(g, locaties);
    }


    public void drawLines(Graphics g, String[] locaties) {
        if (locaties == null || locaties.length < 2) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3)); // Set the line thickness to 3

        ArrayList<Integer> prevCoord = getLocaties(locaties[0]);
        for (int i = 1; i < locaties.length; i++) {
            ArrayList<Integer> currCoord = getLocaties(locaties[i]);

            // If status is "Bewegen" and this is the first line, set color to red
            if (status.equals("Bewegen") && i == 1) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.BLUE); // Reset color to black for other lines
            }

            drawArrow(g2, prevCoord.get(0), prevCoord.get(1), currCoord.get(0), currCoord.get(1));

            // If status is "Pakken" and this is the last location, draw an orange square
            if (status.equals("Pakken") && locaties[i].equals(lastLocation)) {
                g2.setColor(Color.ORANGE);
                g2.fillRect(currCoord.get(0) - 50, currCoord.get(1) - 50, 100, 100);
            }

            // Update previous coordinate to current
            prevCoord = currCoord;
        }
    }



    public void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
        int ARR_SIZE = 6;

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));

        AffineTransform old = g.getTransform(); // Save original transform
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);

        g.setTransform(old); // Restore original transform
    }




    public static ArrayList<Integer> getLocaties(String location) {
        int afstandTussenLocaties = 100;
        int beginx = 550;
        int beginy = 600;
        ArrayList<Integer> xy = new ArrayList<Integer>();
        switch (location) {
            case "A1", "A2", "A3", "A4", "A5" -> xy.add(beginx);
            case "B1", "B2", "B3", "B4", "B5" -> xy.add(beginx - afstandTussenLocaties);
            case "C1", "C2", "C3", "C4", "C5" -> xy.add(beginx - (afstandTussenLocaties * 2));
            case "D1", "D2", "D3", "D4", "D5" -> xy.add(beginx - (afstandTussenLocaties * 3));
            case "E1", "E2", "E3", "E4", "E5" -> xy.add(beginx - (afstandTussenLocaties * 4));
        }

        switch (location) {
            case "A1", "B1", "C1", "D1", "E1" -> xy.add(beginy);
            case "A2", "B2", "C2", "D2", "E2" -> xy.add(beginy - afstandTussenLocaties);
            case "A3", "B3", "C3", "D3", "E3" -> xy.add(beginy - (afstandTussenLocaties * 2));
            case "A4", "B4", "C4", "D4", "E4" -> xy.add(beginy - (afstandTussenLocaties * 3));
            case "A5", "B5", "C5", "D5", "E5" -> xy.add(beginy - (afstandTussenLocaties * 4));
        }

        return xy;

    }
}
