import java.util.List;
import java.util.Scanner;

public class ProductCatalog {
    private List<String> categories;

    public ProductCatalog(List<String> categories) {
        this.categories = categories;
    }

    public void printAllProducts(List<Product> products) {
        
        for (Product product : products) {
            printProductDetails(product);
        }
    }

    public void printProductCategory(List<Product> products, String category) {

        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                printProductDetails(product);
            }
        }
    }

    public void printProductsByName(List<Product> products, String name) {

        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                printProductDetails(product);
            }
        }
    }

    public void printProductsById(List<Product> products, Integer id) {

        for (Product product : products) {
            if (product.getProductId().equals(id)) {
                printProductDetails(product);
            }
        }
    }

    private void printProductDetails(Product product) {
        System.out.println("");
        System.out.println("---------------------------");
        System.out.println("ID: " + product.getProductId());
        System.out.println("Name: " + product.getName());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + product.getPrice());
        System.out.println("Category: " + product.getCategory());
        System.out.println("Stock Level: " + product.getStockLevel());
        System.out.println("Stock: " + product.getStock());
        System.out.println("---------------------------");
    }

    public String getValidCategory(Scanner userInput) {

        String category;
        while (true) {

            System.out.println("Enter product category (Available categories: " + String.join(", ", categories) + "): ");
            category = userInput.nextLine();

            if (categories.contains(category)) {
                break;
            } 
            else {
                System.out.println("Invalid category. Please enter a valid category.");
            }
        }
        return category;
    }
}
