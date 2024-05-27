import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        GUI gui = new GUI();

//                // testcode pakbon genereren
//                String verzender = "Nerdy Gadgets";
//                String ontvanger = "XYZ Winkel";
//
//                // Lijst van items (Artikelcode, Omschrijving, Aantal)
//                List<String[]> items = List.of(
//                        new String[]{"001", "Artikel A", "10"},
//                        new String[]{"002", "Artikel B", "5"},
//                        new String[]{"003", "Artikel C", "20"}
//                );
//
//                // Maak een PakbonGenerator instantie
//                PakbonGenerator pakbon = new PakbonGenerator(verzender, ontvanger, items);
//
//                // Genereer het pakbon tekstbestand
//                pakbon.genereerPakbon("pakbon.txt");

        // Testcode voor BinPacking
        // Maak een lijst om items op te slaan, elk item is een lijst met [ItemID, AantalItems, Afmeting]
        List<List<Integer>> itemsData = new ArrayList<>();

        // Voeg enkele testitems toe aan de lijst
        itemsData.add(List.of(1, 3, 10)); // itemID: 1, aantalItems: 3, afmeting: 10
        itemsData.add(List.of(2, 2, 5));
        itemsData.add(List.of(3, 1, 20));

        // BinPacking met een box capaciteit van 50 (vrij aanpasbaar maar is gelijk voor elke doos.)
        BinPacking binPacking = new BinPacking(50);

        // Voer het beste fit algoritme uit om de items in dozen te plaatsen
        List<Box> verpakteBoxen = binPacking.besteFit(itemsData);

        // Loop door de verpakte dozen en print de inhoud
        for (Box box : verpakteBoxen) {
            System.out.println("Box ID: " + box.getId() + " bevat items:");
            for (List<Integer> item : box.getItems()) {
                System.out.println("Item ID: " + item.get(0) + ", Aantal Items: " + item.get(1) + ", Afmeting: " + item.get(2));
            }
        }





//            // Create a JFrame to hold the custom JPanel
//            JFrame frame = new JFrame("Order Inladen");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1500, 900);
//
//            // Create an instance of the custom JPanel and add it to the frame
//            OrderInladenDialog orderInladenPanel = new OrderInladenDialog(34);
//            frame.add(orderInladenPanel);
//
//            // Display the frame
//            frame.setVisible(true);

    }

}
