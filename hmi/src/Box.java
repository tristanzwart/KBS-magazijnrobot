import java.util.ArrayList;
import java.util.List;

public class Box {
    private int size;
    private int filledTo;
    private List<int[]> products;

    public Box(int size) {
        this.size = size;
        this.filledTo = 0;
        this.products = new ArrayList<>();
    }

    public int getSize() {
        return size;
    }

    public int getFilledTo() {
        return filledTo;
    }

    public void addProduct(int productId, int productSize) {
        this.products.add(new int[]{productId, 1, productSize});
        this.filledTo += productSize;
    }

    public int getRemainingSpace() {
        return size - filledTo;
    }

    public boolean increaseSize(int[] boxSizes) {
        int currentIndex = findCurrentIndex(boxSizes);
        if (currentIndex < boxSizes.length - 1) {
            this.size = boxSizes[currentIndex + 1];
            return true;
        }
        return false;
    }

    private int findCurrentIndex(int[] boxSizes) {
        for (int i = 0; i < boxSizes.length; i++) {
            if (boxSizes[i] == this.size) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Box Size: ").append(size).append(", Filled To: ").append(filledTo).append("\n");
        for (int[] product : products) {
            sb.append("  Product ID: ").append(product[0]).append(", Quantity: ").append(product[1]).append(", Size: ").append(product[2]).append("\n");
        }
        return sb.toString();
    }
}
