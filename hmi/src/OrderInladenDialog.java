import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderInladenDialog extends JDialog {
    OrderInladenPanel panel;
    BinPacking bp;
    TSPAlgorimte tsp;
    List<String[]> routes;
    public OrderInladenDialog(JFrame frame, boolean modal, int OrderID){
        super(frame, modal);
        setTitle("Order "+ OrderID);
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());
        panel = new OrderInladenPanel(OrderID);
        add(panel);
        bp = new BinPacking(OrderID);
        tsp = new TSPAlgorimte();
        routes = new ArrayList<>();
        routes = tsp.calculateAllRoutes(bp.besteFit());

        setVisible(true);


    }
    void routesdoorlopen(List<String[]> routes){


    }
}
