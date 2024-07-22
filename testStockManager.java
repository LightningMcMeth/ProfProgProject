import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class testStockManager {

    private StockManager stockManager;
    private Product product1;
    private Product product2;

    @Before
    public void setUp() {
        HashMap<Integer, Product> initialProducts = new HashMap<>();

        product1 = new Product(1, "Gateron Yellow", "Linear switch", new ArrayList<>(), 2.99f, 100, 500, 300, "switch");
        product2 = new Product(2, "Cherry MX Brown", "Tactile switch", new ArrayList<>(), 4.99f, 100, 500, 400, "switch");

        initialProducts.put(product1.getProductId(), product1);
        initialProducts.put(product2.getProductId(), product2);
        stockManager = new StockManager(initialProducts);
    }

    @Test
    public void testAddProduct() {
        Product newProduct = new Product(3, "Kailh Box Navy", "Clicky switch", new ArrayList<>(), 3.99f, 100, 500, 200, "switch");
        stockManager.addProduct(newProduct);

        assertEquals(newProduct, stockManager.getProductById(3));
    }

    @Test
    public void testRemoveProductById() {
        stockManager.removeProductById(product1.getProductId());

        assertNull(stockManager.getProductById(product1.getProductId()));
    }

    @Test
    public void testContainsProductById() {
        assertTrue(stockManager.containsProductById(product1.getProductId()));
        assertFalse(stockManager.containsProductById(99));
    }

    @Test
    public void testGetProductById() {
        assertEquals(product1, stockManager.getProductById(product1.getProductId()));
    }

    @Test
    public void testIncreaseStock() {
        stockManager.increaseStock(product1.getProductId(), 100);

        assertEquals(Integer.valueOf(400), product1.getStock());
    }

    @Test
    public void testDecreaseStock() {
        stockManager.decreaseStock(product1.getProductId(), 100);

        assertEquals(Integer.valueOf(200), product1.getStock());
    }

    @Test
    public void testGetProductByName() {
        assertEquals(product1, stockManager.getProductByName("Gateron Yellow"));
    }

    @Test
    public void testRemoveProductByName() {
        stockManager.removeProductByName("Gateron Yellow");

        assertNull(stockManager.getProductById(product1.getProductId()));
    }

    @Test
    public void testGenerateId() {
        Integer newId = stockManager.generateId();

        assertNotNull(newId);
        assertFalse(stockManager.containsProductById(newId));
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = stockManager.getAllProducts();
        
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }
}
