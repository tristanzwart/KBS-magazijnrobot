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


        //testcode bin packing algoritme
        List<int[]> products = new ArrayList<>();
        products.add(new int[]{1, 5, 10});  // Product ID, Quantity, Size
        products.add(new int[]{2, 3, 15});
        products.add(new int[]{3, 1, 25});

        BinPacking.runAlgorithm(products);






    }

}
