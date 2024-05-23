import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackingBox {
    private int size;
    private int currentFill;
    private Map<String, Integer> products;

    public PackingBox(int size) {
        this.size = size;
        this.currentFill = 0;
        this.products = new HashMap<>();
    }

    public int getSize() {
        return size;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public boolean canFit(int productSize) {
        return (currentFill + productSize) <= size;
    }

    public void addProduct(String productId, int productSize) {
        products.put(productId, products.getOrDefault(productId, 0) + 1);
        currentFill += productSize;
    }

    public boolean increaseSize(List<Integer> boxAfmetingen) {
        int currentIndex = boxAfmetingen.indexOf(size);
        if (currentIndex < boxAfmetingen.size() - 1) {
            size = boxAfmetingen.get(currentIndex + 1);
            return true;
        }
        return false;
    }

    public int getCurrentFill() {
        return currentFill;
    }
}
