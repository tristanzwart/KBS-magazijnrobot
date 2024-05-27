import java.util.ArrayList;
import java.util.List;

public class BinPacking {
    private int boxCapaciteit; // Grote van elke doos

    // Constructor vor starten van BinPacking
    public BinPacking(int boxCapaciteit) {
        this.boxCapaciteit = boxCapaciteit;
    }

    // Best Fit algoritme om items in dozen te plaatsen
    public List<Box> besteFit(List<List<Integer>> itemsData) {
        List<Box> boxen = new ArrayList<>();
        int boxId = 1;

        // Loop door elk item in de lijst
        for (List<Integer> itemData : itemsData) {
            int itemID = itemData.get(0);
            int aantalItems = itemData.get(1);
            int afmeting = itemData.get(2);

            // Probeer het item in een bestaande doos te plaatsen
            boolean itemGeplaatst = false;
            for (Box box : boxen) {
                if (box.voegItemToe(itemData)) {
                    itemGeplaatst = true;
                    break;
                }
            }

            // Als het item niet in een bestaande doos past, maak dan een nieuwe doos
            if (!itemGeplaatst) {
                Box nieuweBox = new Box(boxId++, boxCapaciteit);
                nieuweBox.voegItemToe(itemData);
                boxen.add(nieuweBox);
            }
        }

        return boxen;
    }
}