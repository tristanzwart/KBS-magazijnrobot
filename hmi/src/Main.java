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

        //testcode BinPacking
        List<List<Integer>> itemsData = new ArrayList<>();
        itemsData.add(List.of(1, 3, 10)); // itemID, aantalItems, afmeting
        itemsData.add(List.of(2, 2, 5));
        itemsData.add(List.of(3, 1, 20));

        BinPacking binPacking = new BinPacking(50); // Aangenomen box capaciteit is 50
        List<Box> verpakteBoxen = binPacking.besteFit(itemsData);

        for (Box box : verpakteBoxen) {
            System.out.println("Box ID: " + box.getId() + " bevat items:");
            for (List<Integer> item : box.getItems()) {
                System.out.println("Item ID: " + item.get(0) + ", Aantal Items: " + item.get(1) + ", Afmeting: " + item.get(2));
            }
        }




    }

}
