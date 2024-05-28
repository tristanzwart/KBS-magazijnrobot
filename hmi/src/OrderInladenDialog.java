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

        bp = new BinPacking(OrderID);
        tsp = new TSPAlgorimte();
        routes = new ArrayList<>();
        routes = tsp.calculateAllRoutes(bp.besteFit());

        panel = new OrderInladenPanel(bp.besteFit());
        add(panel);


        setVisible(true);


    }
    void routesdoorlopen(List<String[]> routes){


    }
}
