import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.ArrayList;

public class testOrderManager {

    private OrderManager orderManager;
    private StockManager stockManager;
    private Product product1;
    private Product product2;
    private Order order1;
    private Order order2;

    @Before
    public void setUp() {
        HashMap<Integer, Product> initialProducts = new HashMap<>();

        product1 = new Product(1, "Gateron Yellow", "Linear switch", new ArrayList<>(), 2.99f, 100, 500, 300, "switch");
        product2 = new Product(2, "Cherry MX Brown", "Tactile switch", new ArrayList<>(), 4.99f, 100, 500, 400, "switch");

        initialProducts.put(product1.getProductId(), product1);
        initialProducts.put(product2.getProductId(), product2);
        stockManager = new StockManager(initialProducts);

        orderManager = new OrderManager();
        order1 = new Order(orderManager.generateOrderId());
        order2 = new Order(orderManager.generateOrderId());

        order1.addProduct(new Product(product1), 10);
        order2.addProduct(new Product(product2), 20);

        orderManager.createOrder(order1);
        orderManager.createOrder(order2);
    }

    @Test
    public void testGenerateOrderId() {
        Integer newOrderId = orderManager.generateOrderId();
        
        assertNotNull(newOrderId);
        assertFalse(orderManager.getPendingOrders().containsKey(newOrderId));
    }

    @Test
    public void testCreateOrder() {
        Order newOrder = new Order(orderManager.generateOrderId());
        orderManager.createOrder(newOrder);

        assertEquals(newOrder, orderManager.getOrderById(newOrder.getOrderId()));
    }

    @Test
    public void testGetPendingOrders() {
        HashMap<Integer, Order> pendingOrders = orderManager.getPendingOrders();

        assertTrue(pendingOrders.containsKey(order1.getOrderId()));
        assertTrue(pendingOrders.containsKey(order2.getOrderId()));
    }

    @Test
    public void testGetCompletedOrders() {
        HashMap<Integer, Order> completedOrders = orderManager.getCompletedOrders();
        assertTrue(completedOrders.isEmpty());

        orderManager.completeOrder(order1);
        completedOrders = orderManager.getCompletedOrders();

        assertTrue(completedOrders.containsKey(order1.getOrderId()));
    }

    @Test
    public void testGetOrderById() {
        assertEquals(order1, orderManager.getOrderById(order1.getOrderId()));
    }

    @Test
    public void testRemovePendingOrderById() {
        orderManager.removePendingOrderById(order1.getOrderId(), stockManager);

        assertNull(orderManager.getOrderById(order1.getOrderId()));
        assertEquals(Integer.valueOf(310), stockManager.getProductById(product1.getProductId()).getStock());
    }

    @Test
    public void testCompleteOrder() {
        orderManager.completeOrder(order2);

        assertNull(orderManager.getOrderById(order2.getOrderId()));
        assertEquals(OrderStatus.COMPLETED, orderManager.getCompletedOrders().get(order2.getOrderId()).getOrderStatus());
    }

    @Test
    public void testUpdateOrderStatus() {
        orderManager.updateOrderStatus(order2, OrderStatus.SHIPPING);
        assertEquals(OrderStatus.SHIPPING, orderManager.getOrderById(order2.getOrderId()).getOrderStatus());

        orderManager.updateOrderStatus(order2, OrderStatus.COMPLETED);
        assertEquals(OrderStatus.COMPLETED, orderManager.getCompletedOrders().get(order2.getOrderId()).getOrderStatus());
    }

    @Test
    public void testListPendingOrders() {
        orderManager.listPendingOrders();
        
        assertTrue(orderManager.getPendingOrders().containsKey(order1.getOrderId()));
        assertTrue(orderManager.getPendingOrders().containsKey(order2.getOrderId()));
    }

    @Test
    public void testPrintPendingOrderById() {
        orderManager.printPendingOrderById(order1.getOrderId());
        assertEquals(order1, orderManager.getOrderById(order1.getOrderId()));
    }
}
