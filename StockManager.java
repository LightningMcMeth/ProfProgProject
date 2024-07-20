import java.util.HashMap;

public class StockManager {
    
    private HashMap<Integer, Product> products;
    private Integer lastUsedId = 0;

    public StockManager() {
        products = new HashMap<Integer, Product>();
    }

    public void addProduct(Product newProduct) {
        products.put(newProduct.getProductId(), newProduct);
    }

    public void removeProductById(Integer productId) {
        products.remove(productId);
    }

    public boolean containsProductById(Integer productId) {
        return products.containsKey(productId);
    }

    public Product getProductById(Integer productId) {
        return products.get(productId);
    }

    public void increaseStock(Integer productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity value. Qunatity should be larger than 0.");       
        }

        Product product = getProductById(productId);

        if (product != null) {
            product.setStock(product.getStock() + quantity);

            alertQuantity(product.getStock(), product.getLowerThreshold(), product.getUpperThreshold(), product.getName());
        } 
        else {
            System.out.println("Product not found.");
        }
    }

    public void decreaseStock(Integer productId, Integer quantity) {
        Product product = getProductById(productId);
        
        if (product != null) {

            if (product.getStock() > quantity) {
                product.setStock(product.getStock() - quantity);

                alertQuantity(product.getStock(), product.getLowerThreshold(), product.getUpperThreshold(), product.getName());
            } 
            else {
                System.out.println("Invalid quantity value. Product stock cannot be lower than 0");
            }
        } 
        else {
            System.out.println("Product not found.");
        }
    }

    private void alertQuantity(Integer stock, Integer lowerBound, Integer upperBound, String name) {

        if (stock <= lowerBound) {
            System.out.println(name + " stock is low.");
        } 
        else if (stock >= upperBound) {
            System.out.println(name + " is overstocked.");
        }
    }

    public Product getProductByName(String productName) {

        for (Product product : products.values()) {
            
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    public void removeProductByName(String productName) {

        for (Product product : products.values()) {
            
            if (product.getName().equals(productName)) {
                products.remove(product.getProductId());
            }
        }
    }

    public Integer generateId() {

        while (products.containsKey(lastUsedId)) {
            lastUsedId++;
        }
        return lastUsedId;
    }
}