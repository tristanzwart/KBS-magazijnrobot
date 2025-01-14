import java.util.ArrayList;
import java.util.List;

public class BinPacking {
    private int boxCapaciteit = 30; // Grote van elke doos
    private  List<List<Integer>> itemsData = new ArrayList<>();
    private Database database;


    // Constructor vor starten van BinPacking
    public BinPacking(int OrderID) {
        database = new Database();
        itemsData = database.voorBinPacking(OrderID);
    }



    // Best Fit algoritme om items in dozen te plaatsen
    public List<Bin> besteFit() {
        List<Bin> boxen = new ArrayList<>();
        int boxId = 1;

        // Loop door elk item in de lijst
        for (List<Integer> itemData : itemsData) {
            int itemID = itemData.get(0);
            int aantalItems = itemData.get(1);
            int afmeting = itemData.get(2);

            // Probeer het item in een bestaande doos te plaatsen
            boolean itemGeplaatst = false;
            for (Bin bin : boxen) {
                if (bin.voegItemToe(itemData)) {
                    itemGeplaatst = true;
                    break;
                }
            }

            // Als het item niet in een bestaande doos past, maak dan een nieuwe doos
            if (!itemGeplaatst) {
                Bin nieuweBin = new Bin(boxId++, boxCapaciteit);
                nieuweBin.voegItemToe(itemData);
                boxen.add(nieuweBin);
            }
        }

        return boxen;
    }
}