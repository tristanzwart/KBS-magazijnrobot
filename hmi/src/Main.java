import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        GUI gui = new GUI();



                // Definieer de pakbon details
                String verzender = "Nerdy Gadgets";
                String ontvanger = "XYZ Winkel";

                // Lijst van items (Artikelcode, Omschrijving, Aantal)
                List<String[]> items = List.of(
                        new String[]{"001", "Artikel A", "10"},
                        new String[]{"002", "Artikel B", "5"},
                        new String[]{"003", "Artikel C", "20"}
                );

                // Maak een PakbonGenerator instantie
                PakbonGenerator pakbon = new PakbonGenerator(verzender, ontvanger, items);

                // Genereer het pakbon tekstbestand
                pakbon.genereerPakbon("pakbon.txt");







    }

}
