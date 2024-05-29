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
        pakbonmaken(bp.besteFit(), OrderID);


        panel = new OrderInladenPanel(bp.besteFit());
        add(panel);


        setVisible(true);


    }
    void pakbonmaken(List<Bin> bestfit,int Orderid) {
        Database db = new Database();
        String verzender = "Nerdy Gadgets";
        String ontvanger = db.getcustomername(Orderid);

        for (int i = 0; i < bestfit.size(); i++) {
            List<String[]> items = new ArrayList<>();


            for (List<Integer> item : bestfit.get(i).getItems()) {
                // Retrieve the orderId from the item
                int orderId = item.get(0);

                // Call the getOrderLineInfo method from the database with the orderId
                String[] orderLineInfoArray = db.getOrderLineInfo(orderId);
                items.add(orderLineInfoArray);

                PakbonGenerator pakbon = new PakbonGenerator(verzender, ontvanger, items);
                pakbon.genereerPakbon( "pakbon"+ (i+1)+".txt");


            }
            PakbonGenerator pakbon = new PakbonGenerator(verzender, ontvanger, items);
            pakbon.genereerPakbon( "Order "+Orderid + " Doos "+ (i+1)+".txt");



            // Convert List<Object[]> to Object[][]



        }
    }
}
