import java.util.ArrayList;
import java.util.List;

public class BinPacking {
    private int boxCapaciteit = 30; // Grote van elke doos
    private static List<List<Integer>> itemsData = new ArrayList<>();

    // Constructor vor starten van BinPacking
    public BinPacking(int boxCapaciteit) {
        this.boxCapaciteit = boxCapaciteit;
    }

    public static void UpdateItemData(int ID, int quantity, int afmeting){
        itemsData.add(List.of(ID, quantity, afmeting));
    }

    public void ClearItemsData(){
        itemsData.clear();
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