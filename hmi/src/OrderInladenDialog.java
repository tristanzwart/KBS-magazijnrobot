import javax.swing.*;
import java.awt.*;

public class OrderInladenDialog extends JDialog {
    OrderInladenPanel panel;
    public OrderInladenDialog(JFrame frame, boolean modal, int OrderID){
        super(frame, modal);
        setTitle("Order "+ OrderID);
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());
        panel = new OrderInladenPanel(OrderID);
        add(panel);
        setVisible(true);


    }
}
