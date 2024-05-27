import java.util.ArrayList;
import java.util.List;

public class Box {
    private int id;
    private int capaciteit;
    private int huidigeBelasting;
    private List<List<Integer>> items; // Lijst van [ItemID, AantalItems, Afmeting]

    public Box(int id, int capaciteit) {
        this.id = id;
        this.capaciteit = capaciteit;
        this.huidigeBelasting = 0;
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getCapaciteit() {
        return capaciteit;
    }

    public int getHuidigeBelasting() {
        return huidigeBelasting;
    }

    public List<List<Integer>> getItems() {
        return items;
    }

    public boolean voegItemToe(List<Integer> item) {
        int afmeting = item.get(2);
        if (huidigeBelasting + afmeting <= capaciteit) {
            items.add(item);
            huidigeBelasting += afmeting;
            return true;
        }
        return false;
    }
}
