import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinPacking {
    private static final int[] BOX_SIZES = {5, 15, 30};

    public static void runAlgorithm(List<int[]> products) {
        if (isAnyProductTooLarge(products)) {
            System.out.println("Error: One or more products are too large to fit in the largest box size.");
            return;
        }

        sortProductsBySize(products);
        List<Box> boxes = packProducts(products);

        printResults(boxes);
    }

    private static boolean isAnyProductTooLarge(List<int[]> products) {
        int largestBoxSize = BOX_SIZES[BOX_SIZES.length - 1];
        for (int[] product : products) {
            if (product[2] > largestBoxSize) {
                return true;
            }
        }
        return false;
    }

    private static void sortProductsBySize(List<int[]> products) {
        products.sort(Comparator.comparingInt(a -> -a[2]));
    }

    private static List<Box> packProducts(List<int[]> products) {
        List<Box> boxes = new ArrayList<>();
        Box currentBox = new Box(BOX_SIZES[0]);
        boxes.add(currentBox);

        for (int[] product : products) {
            int productId = product[0];
            int quantity = product[1];
            int size = product[2];

            for (int i = 0; i < quantity; i++) {
                boolean productPlaced = false;

                for (Box box : boxes) {
                    if (box.getRemainingSpace() >= size) {
                        box.addProduct(productId, size);
                        productPlaced = true;
                        break;
                    }
                }

                if (!productPlaced) {
                    while (currentBox.getRemainingSpace() < size && currentBox.increaseSize(BOX_SIZES)) {
                        // Increase the current box size until it fits or reaches the maximum size
                    }
                    if (currentBox.getRemainingSpace() >= size) {
                        currentBox.addProduct(productId, size);
                    } else {
                        currentBox = new Box(BOX_SIZES[0]);
                        currentBox.addProduct(productId, size);
                        boxes.add(currentBox);
                    }
                }
            }
        }
        return boxes;
    }

    private static void printResults(List<Box> boxes) {
        System.out.println("Total boxes used: " + boxes.size());
        for (int i = 0; i < boxes.size(); i++) {
            System.out.println("Box " + (i + 1) + ":");
            System.out.print(boxes.get(i).toString());
        }
    }
}
