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

        bp = new BinPacking(OrderID);
        tsp = new TSPAlgorimte();
        routes = new ArrayList<>();
        routes = tsp.calculateAllRoutes(bp.besteFit());
        pakbonmaken(bp.besteFit(), OrderID);


        panel = new OrderInladenPanel(bp.besteFit());
        add(panel);

        RouteNaarRobot(gui.getArduino1(), gui.getArduino2());

        setVisible(true);
    }

    public void RouteNaarRobot(ArduinoCom arduino1, ArduinoCom arduino2) {
        final ArduinoCom finalArduino1 = arduino1;
        final ArduinoCom finalArduino2 = arduino2;
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            for (String[] route : routes) {
                panel.setLocations(route);
                if (route.length >= 1) {  // Ensure each route has at least 1 strings
                    for (int i = 0; i < route.length; i++) {
                        System.out.println(route[i]);

                        finalArduino1.verstuurData(ArduinoCom.getCoordinates(route[i].charAt(1)));
                        finalArduino2.verstuurData(ArduinoCom.getCoordinates(route[i].charAt(0)));
                        System.out.println("Sending " + ArduinoCom.getCoordinates(route[i].charAt(1)) + " to arduino1");
                        System.out.println("Sending " + ArduinoCom.getCoordinates(route[i].charAt(0)) + " to arduino2");
                        panel.setStatus("Bewegen");
                        latch = new CountDownLatch(1);

                        try {
                            latch.await();  // Wait for the "ready" signal
                            panel.setLastLocation(route[i]);
                            panel.setStatus("Pakken");
                            arduino1.verstuurData("oppakken");
                            System.out.println("Sending 'oppakken' to arduino2");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Thread interrupted");
                        }
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
            return null;
        }
    };
    worker.execute();
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
        if(readyReceived1 == true && readyReceived2 == true){
            readyReceived1 = false;
            readyReceived2 = false;
            System.out.println("Ready status reset");
        }
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
