import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderInladenPanel extends JPanel {
    private JPanel magazijn;

    //Tabel voor de dozen
    private ArrayList<JTable> doosTabellen;

    public OrderInladenPanel(List<Bin> bin) {
        doosTabellen = new ArrayList<>();
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

        //Visualisatie van de dozen
        //Voor elke doos deze for loop uitvoeren
        for(int i = 0; i < bin.size(); i++) {
            JLabel doosLabel = new JLabel("Doos " + i);
            doosLabel.setBounds(1100, 30 + i * 150, 100, 50);

            add(doosLabel);

            //Tabel voor de doos maken

            Object[][] data = {};



            String[] columnNames = {"Artikelnummer", "Artikelnaam", "Op vooraad"};

            JTable doosTabel = new JTable(new DefaultTableModel(data, columnNames));
            doosTabel.setDefaultEditor(Object.class, null);
            doosTabel.getTableHeader().setReorderingAllowed(false);
            JScrollPane scrollPane = new JScrollPane(doosTabel);
            scrollPane.setBounds(885, 80 + i * 150, 500, 100);
            add(scrollPane);
            doosTabellen.add(doosTabel);


            //Updaten van de tabel data voorlopig niet nodig
            //DefaultTableModel model = (DefaultTableModel) doosTabel.getModel();
            //model.setDataVector(data, columnNames);
            //giveSideFeedback("");

        }



    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }


}