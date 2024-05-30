import java.util.ArrayList;
import java.util.List;

public class Bin {
    private int id; // Doos nummer
    private int capaciteit; // Maximale capaciteit van doos
    private int huidigeBelasting; // Gebruikte ruimte van de doos
    private List<List<Integer>> items; // Lijst van items in de doos, elk item is een lijst met [ItemID, AantalItems, Afmeting]

    // Constructor om een nieuwe doos te maken
    public Bin(int id, int capaciteit) {
        this.id = id;
        this.capaciteit = capaciteit;
        this.huidigeBelasting = 0;
        this.items = new ArrayList<>();
    }

    // Getter voor doos nummer
    public int getId() {
        return id;
    }

    // Getter voor capaciteit van doos
    public int getCapaciteit() {
        return capaciteit;
    }

    // Getter voor huidige belasting van doos
    public int getHuidigeBelasting() {
        return huidigeBelasting;
    }

    // Getter voor items in doos
    public List<List<Integer>> getItems() {
        return items;
    }

    // Functie om een item toe te voegen aan de doos
    public boolean voegItemToe(List<Integer> item) {
        int afmeting = item.get(2);
        // Controleer of het item past in de doos
        if (huidigeBelasting + afmeting <= capaciteit) {
            items.add(item);
            huidigeBelasting += afmeting;
            return true;
        }
        return false;
    }
}