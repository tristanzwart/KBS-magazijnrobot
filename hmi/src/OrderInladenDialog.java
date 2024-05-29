import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OrderInladenDialog extends JDialog {
    OrderInladenPanel panel;
    BinPacking bp;
    GUI gui;
    TSPAlgorimte tsp;
    List<String[]> routes;
    private static CountDownLatch latch;
    static boolean readyReceived1 = false;
    static boolean readyReceived2 = false;

    public OrderInladenDialog(JFrame frame, boolean modal, int OrderID, GUI gui){
        super(frame, modal);
        this.gui = gui;
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
        RouteNaarRobot(gui.getArduino1(), gui.getArduino2());

        setVisible(true);
    }

    public void RouteNaarRobot(ArduinoCom arduino1, ArduinoCom arduino2) {
        for (String[] route : routes) {
            if (route.length >= 3) {  // Ensure each route has exactly 3 strings
                for (int i = 0; i < route.length; i++) {
                    System.out.println(route[i]);

                    arduino1.verstuurData(ArduinoCom.getCoordinates(route[i].charAt(1)));
                    arduino2.verstuurData(ArduinoCom.getCoordinates(route[i].charAt(0)));
                    System.out.println("Sending " + ArduinoCom.getCoordinates(route[i].charAt(1)) + " to arduino1");
                    System.out.println("Sending " + ArduinoCom.getCoordinates(route[i].charAt(0)) + " to arduino2");
                    latch = new CountDownLatch(1);

                    //arduino2.verstuurData("oppakken");
                    //System.out.println("Sending 'oppakken' to arduino2");

                    latch = new CountDownLatch(1);

                    try {
                        latch.await();  // Wait for the "bewegen" signal
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted");
                    }
                }
            } else {
                System.out.println("Invalid route length: " + route.length);
            }
        }
    }

    public static void onBewegenReceived(int arduino) {
        if (arduino == 1){
            readyReceived1 = true;
        } else if (arduino == 2) {
            readyReceived2 = true;
        }
        System.out.println("Ready status: " + readyReceived1 + " " + readyReceived2);
        if (readyReceived1 == true && readyReceived2 == true || arduino == 0){
            if (latch != null) {
                latch.countDown();  // Signal that "bewegen" was received
                System.out.println("Latch triggerd!");
            }
        }
    }
}
