import java.util.*;

public class BinPacking {

    public static void processOrder(List<List<Object>> products) {
        // Step 1: Check how many products are in the order
        int totalProducts = 0;
        for (List<Object> product : products) {
            totalProducts += (int) product.get(1);
        }
        System.out.println("Total products: " + totalProducts);

        // Step 2: Sort the order by size (biggest product first)
        products.sort((a, b) -> (int) b.get(2) - (int) a.get(2));

        // Display sorted products
        System.out.println("Sorted products: ");
        for (List<Object> product : products) {
            System.out.println("Product ID: " + product.get(0) + ", Quantity: " + product.get(1) + ", Size: " + product.get(2));
        }

        // Placeholder for package sizes (you can replace this with actual package sizes)
        List<Integer> boxAfmetingen = Arrays.asList(5, 15, 30);
        boxAfmetingen.sort(Comparator.naturalOrder());

        // Display sorted package sizes
        System.out.println("Sorted box sizes: " + boxAfmetingen);

        // Step 3: Check for items bigger than the largest box size
        int largestBoxSize = boxAfmetingen.get(boxAfmetingen.size() - 1);
        boolean hasOversizedItems = false;

        for (List<Object> product : products) {
            int productSize = (int) product.get(2);
            if (productSize > largestBoxSize) {
                System.out.println("Error: Product " + product.get(0) + " with size " + productSize +
                        " is larger than the largest box size " + largestBoxSize);
                hasOversizedItems = true;
            }
        }

        if (hasOversizedItems) {
            System.out.println("Stopping program due to oversized items.");
            return;
        }

        // Step 4: Distribute products into boxes
        List<PackingBox> usedBoxes = new ArrayList<>();
        usedBoxes.add(new PackingBox(boxAfmetingen.get(0)));  // Start with the smallest box size

        for (List<Object> product : products) {
            String productId = (String) product.get(0);
            int quantity = (int) product.get(1);
            int size = (int) product.get(2);

            for (int i = 0; i < quantity; i++) {
                boolean productPlaced = false;

                // Try to place the product in the existing boxes
                for (PackingBox box : usedBoxes) {
                    if (box.canFit(size)) {
                        box.addProduct(productId, size);
                        productPlaced = true;
                        break;
                    }
                }

                // If the product was not placed, handle increasing the box size or adding a new box
                if (!productPlaced) {
                    for (PackingBox box : usedBoxes) {
                        while (!box.canFit(size) && box.increaseSize(boxAfmetingen)) {
                            // Increase the box size until it fits or reaches the maximum size
                        }
                        if (box.canFit(size)) {
                            box.addProduct(productId, size);
                            productPlaced = true;
                            break;
                        }
                    }
                }

                if (!productPlaced) {
                    PackingBox newBox = new PackingBox(boxAfmetingen.get(0));
                    newBox.addProduct(productId, size);
                    usedBoxes.add(newBox);
                }
            }
        }

        // Print results
        printResults(usedBoxes);
    }

    public static void printResults(List<PackingBox> usedBoxes) {
        System.out.println("Total boxes used: " + usedBoxes.size());
        for (int i = 0; i < usedBoxes.size(); i++) {
            PackingBox box = usedBoxes.get(i);
            System.out.println("Box " + (i + 1) + ": Size " + box.getSize() + " (Filled: " + box.getCurrentFill() + ")");
            for (Map.Entry<String, Integer> entry : box.getProducts().entrySet()) {
                System.out.println("  Product ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
            }
        }
    }
}
