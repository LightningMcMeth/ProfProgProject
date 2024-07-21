import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
//For when I'm done: make a loading sequence where an ascii logo is printed line by line into the console

public class Main {
    public static void main(String[] args) {
        
        boolean continueRunning = true;
        Command userCommand = null;
        List<String> placeholderCategories = new ArrayList<>();
        placeholderCategories.add("switch");
        placeholderCategories.add("keyboard");
        placeholderCategories.add("deskmat");

        StockManager stockManager = new StockManager();
        ProductCatalog productCatalog = new ProductCatalog(placeholderCategories);
        Scanner userInput = new Scanner(System.in);

        while (continueRunning) {
            System.out.println("Enter command (enter 'help' for info): ");
            
            userCommand = Command.fromString(userInput.nextLine());

            if (userCommand == null) {
                System.out.println("Invalid command :( try again, but this time type properly.");
                continue;
            }

            switch(userCommand) {
                case HELP:
                    printHelp();
                    break;

                case ADD:
                    addProduct(userInput, stockManager, productCatalog);
                    break;

                case REMOVE:
                    removeProduct(userInput, stockManager);
                    break;

                case EDIT:

                    break;

                case STOCK:
                    manageStock(userInput, stockManager);
                    break;

                case CATALOG:
                    manageCatalog(userInput, productCatalog, stockManager);
                    break;

                case EXIT:
                    continueRunning = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid command :( try again, but this time type properly.");
            }
        }

        userInput.close();
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("HELP - Display this help message");
        System.out.println("ADD - Add a new product");
        System.out.println("REMOVE - Remove a product");
        System.out.println("STOCK - Manage stock of a product");
        System.out.println("CATALOG - View product catalog");
        System.out.println("EDIT - Edit a product");
        System.out.println("EXIT - Exit the program");
    }

    private static void addProduct(Scanner userInput, StockManager stockManager, ProductCatalog productCatalog) {

        Integer id = stockManager.generateId();
        System.out.println("Enter product name: ");
        String name = userInput.nextLine();
        System.out.println("Enter product description: ");
        String description = userInput.nextLine();

        Float price = null;
        while (price == null || price < 0.1f) {

            System.out.println("Enter product price (must be at least 0.1): ");
            price = Float.parseFloat(userInput.nextLine());

            if (price < 0.1f) {
                System.out.println("Invalid price. Please enter a value of at least 0.1.");
            }
        }

        Integer lowerThreshold = 0;
        Integer upperThreshold = 0;
        while (lowerThreshold >= upperThreshold || lowerThreshold <= 0 || upperThreshold <= 0) {

            System.out.println("Enter lower stock threshold: ");
            lowerThreshold = Integer.parseInt(userInput.nextLine());
            System.out.println("Enter upper stock threshold: ");
            upperThreshold = Integer.parseInt(userInput.nextLine());

            if (lowerThreshold >= upperThreshold) {
                System.out.println("Invalid thresholds. Lower threshold must be less than upper threshold.");
            }
        }

        Integer stock = 0;
        while (stock <= 0) {
            System.out.println("Enter initial stock: ");
            stock = Integer.parseInt(userInput.nextLine());

            if (stock <= 0) {
                System.out.println("Invalid stock. Please enter a value higher than 0.");
            }
        }

        String category = productCatalog.getValidCategory(userInput);

        List<String> additionalParameters = new ArrayList<String>();
        System.out.println("Do you want to add additional parameters? (yes/no)");
        String addParams = userInput.nextLine();

        if (addParams.equalsIgnoreCase("yes")) {

            while (true) {

                System.out.println("Enter an additional parameter (or type 'done' to finish): ");
                String param = userInput.nextLine();

                if (param.equalsIgnoreCase("done")) {
                    break;
                }
                additionalParameters.add(param);
            }
        }

        Product newProduct = new Product(id, name, description, additionalParameters, price, lowerThreshold, upperThreshold, stock, category);
        
        stockManager.addProduct(newProduct);
        System.out.println("Product " + newProduct.getName() + " added successfully");
    }

    public static void removeProduct(Scanner userInput, StockManager stockManager) {
        
        Product product = getProductByIdOrName(userInput, stockManager);
        stockManager.removeProductById(product.getProductId());

        System.out.println("Product removed successfully.");
    }

    public static void manageStock(Scanner userInput, StockManager stockManager) {

        Product product = getProductByIdOrName(userInput, stockManager);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        Integer oldStock = product.getStock();

        System.out.println("Do you want to increase or descrease product stock? (increase/dicrease): ");
        String command = userInput.nextLine();

        Integer quantity = 0;
        while (quantity <= 0) {
            System.out.println("Enter quantity: ");
            quantity = Integer.parseInt(userInput.nextLine());

            if (quantity <= 0) {
                System.out.println("Invalid quantity value. Enter a value higher than 0.");
            }
        }
        
        if (command.equalsIgnoreCase("increase")) {
            stockManager.increaseStock(product.getProductId(), quantity);
        }
        else if (command.equalsIgnoreCase("decrease")) {
            stockManager.decreaseStock(product.getProductId(), quantity);
        }

        System.out.println(product.getName() + " stock has been modified " + oldStock + " -> " + product.getStock());
    }

    public static void editProduct(Scanner userInput, StockManager stockManager) {

    }

    public static Product getProductByIdOrName(Scanner userInput, StockManager stockManager) {

        System.out.println("Enter product ID or name: ");
        String productIdentifier = userInput.nextLine();
        Product product = null;

        try {
            Integer productId = Integer.parseInt(productIdentifier);
            product = stockManager.getProductById(productId);

        } catch (NumberFormatException e) {
            product = stockManager.getProductByName(productIdentifier);
        }

        return product;
    }

    public static void manageCatalog(Scanner userInput, ProductCatalog productCatalog, StockManager stockManager) {

        System.out.println("Enter search parameter (all, name, category, id): ");
        String parameter = userInput.nextLine();

        switch (parameter.toLowerCase()) {
            case "all":
                productCatalog.printAllProducts(stockManager.getAllProducts());
                break;

            case "name":
                System.out.println("Enter product name: ");
                String name = userInput.nextLine();

                productCatalog.printProductsByName(stockManager.getAllProducts(), name);
                break;

            case "category":
                System.out.println("Enter product category: ");
                String category = userInput.nextLine();

                productCatalog.printProductCategory(stockManager.getAllProducts(), category);
                break;

            case "id":
                System.out.println("Enter product ID: ");
                Integer id = Integer.parseInt(userInput.nextLine());

                productCatalog.printProductsById(stockManager.getAllProducts(), id);
                break;

            default:
                System.out.println("Invalid parameter.");
        }
    }
}
