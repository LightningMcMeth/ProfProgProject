import java.util.HashMap;

public class OrderManager {
    private HashMap<Integer, Order> pendingOrders;
    private HashMap<Integer, Order> completedOrders;
    private Integer lastUsedOrderId;

    public OrderManager() {
        this.pendingOrders = new HashMap<>();
        this.completedOrders = new HashMap<>();
        this.lastUsedOrderId = 0;
    }

    public Integer generateOrderId() {
        return ++lastUsedOrderId;
    }

    public void createOrder(Order order) {
        this.pendingOrders.put(order.getOrderId(), order);
    }

    public HashMap<Integer, Order> getPendingOrders() {
        return pendingOrders;
    }

    public HashMap<Integer, Order> getCompletedOrders() {
        return completedOrders;
    }

    public Order getOrderById(Integer id) {
        return pendingOrders.get(id);
    }

    public void removePendingOrderById(Integer id, StockManager stockManager) {
        Order order = this.pendingOrders.remove(id);
    
        if (order != null) {
            for (Product product : order.getProducts()) {
                stockManager.increaseStock(product.getProductId(), product.getStock());
            }
            System.out.println("Order deleted successfully.");
        } 
        else {
            System.out.println("Order not found.");
        }
    }

    public void completeOrder(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETED);
        this.pendingOrders.remove(order.getOrderId());
        this.completedOrders.put(order.getOrderId(), order);
    }

    public void updateOrderStatus(Order order, OrderStatus status) {
        order.setOrderStatus(status);
        if (status == OrderStatus.COMPLETED) {
            completeOrder(order);
        }
    }

    public void listPendingOrders() {
        System.out.println("Pending orders: ");

        for (Order order : pendingOrders.values()) {
            printOrder(order);
        }
    }

    public void printPendingOrderById(Integer id) {
        Order order = pendingOrders.get(id);

        if (order != null) {
            printOrder(order);
        }
    }

    private void printOrder(Order order) {
        System.out.println("===================");
        System.out.println("Order ID: " + order.getOrderId());
        Float total = order.calculateTotal();

        for (Product product : order.getProducts()) {
            System.out.println("Product: " + product.getName() + ", Quantity: " + product.getStock());
        }
        System.out.println("Order total >>> " + total);
        System.out.println("Order status: " + order.getOrderStatus());
        System.out.println();
    }
}
