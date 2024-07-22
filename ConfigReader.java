import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class ConfigReader {

    public ConfigReader(){

    }

    public void loadConfig(String filePath, ProductCatalog productCatalog, StockManager stockManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {

            String line = reader.readLine();
            String[] categories = line.split(", ");

            for (String category : categories) {
                productCatalog.addCategory(category);
            }

            String productLine;
            while ((productLine = reader.readLine()) != null) {

                String[] productData = productLine.split(", ");
                Integer productId = Integer.parseInt(productData[0]);
                String name = productData[1];
                String description = productData[2];
                Float price = Float.parseFloat(productData[3]);
                Integer lowerThreshold = null;
                Integer upperThreshold = null;

                if (!productData[4].isEmpty()) {
                    String[] thresholds = productData[4].split("-");
                    lowerThreshold = Integer.parseInt(thresholds[0]);
                    upperThreshold = Integer.parseInt(thresholds[1]);
                }

                Integer stock = Integer.parseInt(productData[5]);
                List<String> additionalParameters = new ArrayList<>();

                if (productData.length > 6 && !productData[6].isEmpty()) {
                    String[] additionalParamsArray = productData[6].split("-");
                    for (String param : additionalParamsArray) {
                        additionalParameters.add(param);
                    }
                }

                String category = productData[7];
                Product product = new Product(productId, name, description, additionalParameters, price, lowerThreshold, upperThreshold, stock, category);
                stockManager.addProduct(product);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        catch (NumberFormatException e) {
            System.out.println("Error parsing config file: " + e.getMessage());
        }
    }

    public void saveConfig(String filePath, ProductCatalog productCatalog, StockManager stockManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {

            List<String> categories = productCatalog.getCategories();
            writer.write(String.join(", ", categories));
            writer.newLine();

            List<Product> products = stockManager.getAllProducts();
            for (Product product : products) {
                String additionalParameters = String.join("-", product.getAdditionalParameters());

                writer.write(product.getProductId() + ", " +
                             product.getName() + ", " +
                             product.getDescription() + ", " +
                             product.getPrice() + ", " +
                             (product.getStockThresholds() != null ? product.getLowerThreshold() + "-" + product.getUpperThreshold() : "") + ", " +
                             product.getStock() + ", " +
                             additionalParameters + ", " + 
                             product.getCategory());
                writer.newLine();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logCompletedOrders(String filePath, OrderManager orderManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            HashMap<Integer, Order> completedOrders = orderManager.getCompletedOrders();

            for (Order order : completedOrders.values()) {
                StringBuilder orderLine = new StringBuilder();
                orderLine.append("Order ID: ").append(order.getOrderId()).append(", ");
                float totalPrice = 0.0f;

                for (Product product : order.getProducts()) {
                    Integer quantity = product.getStock();
                    orderLine.append(product.getName()).append(" - qty: ").append(quantity).append(", ");
                    totalPrice += product.getPrice() * quantity;
                }

                orderLine.append("total: ").append(totalPrice);
                writer.write(orderLine.toString());
                writer.newLine();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
