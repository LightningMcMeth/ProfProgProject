import java.util.ArrayList;
import java.util.List;

public class Order {
    private Integer orderId;
    private List<Product> products;
    private OrderStatus orderStatus;

    public Order(Integer orderId) {
        this.orderId = orderId;
        this.products = new ArrayList<>();
        this.orderStatus = OrderStatus.PROCESSING;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void addProduct(Product product, Integer quantity) {
        Product productCopy = new Product(product);
        productCopy.setStock(quantity);
        this.products.add(productCopy);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Product getProductByName(String name) {
        for (Product product : products) {

            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public Float calculateTotal() {
        Float total = 0.0f;
        for (Product product : products) {
            total += product.getPrice() * product.getStock();
        }

        return total;
    }
}
