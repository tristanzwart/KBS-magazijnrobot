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
        if (routes == null || routes.isEmpty()) {
            System.out.println("No routes to display.");
            return;
        }

        System.out.println("Routes:");
        for (int i = 0; i < routes.size(); i++) {
            String[] route = routes.get(i);
            System.out.print("Route " + (i + 1) + ": ");
            if (route == null || route.length == 0) {
                System.out.println("Empty route");
            } else {
                for (int j = 0; j < route.length; j++) {
                    System.out.print(route[j]);
                    if (j < route.length - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            }
        }

        setVisible(true);


    }
    void routesdoorlopen(List<String[]> routes){


    }
}
