import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        String configFilePath = "productConfig.txt";
        String orderLogFilePath = "orderLogs.txt";
        ConfigReader configReader = new ConfigReader();
        
        boolean continueRunning = true;
        Command userCommand = null;
        List<String> categories = new ArrayList<>();
        HashMap<Integer, Product> initialProducts = new HashMap<>();

        StockManager stockManager = new StockManager(initialProducts);
        ProductCatalog productCatalog = new ProductCatalog(categories);
        configReader.loadConfig(configFilePath, productCatalog, stockManager);

        OrderManager orderManager = new OrderManager();
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
                    editProduct(userInput, stockManager, productCatalog);
                    break;

                case STOCK:
                    manageStock(userInput, stockManager);
                    break;

                case CATALOG:
                    viewCatalog(userInput, productCatalog, stockManager);
                    break;
                
                case ORDER:
                    manageOrders(userInput, stockManager, orderManager);
                    break;
                
                case SAVE:
                    saveConfig(configReader, configFilePath, productCatalog, stockManager);
                    break;

                case LOAD:
                    loadConfig(configReader, configFilePath, productCatalog, stockManager);
                    break;
                
                case PATH:
                    editFilePaths(userInput, configFilePath, orderLogFilePath);
                    break;

                case EXIT:
                    continueRunning = false;
                    configReader.logCompletedOrders(orderLogFilePath, orderManager);
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
        System.out.println("ORDER - Create or manage orders.");
        System.out.println("EXIT - Exit the program");
        System.out.println("SAVE - Saves current products and categories to the config file.");
        System.out.println("LOAD - load products and categories from the config file.");
    }

    private static void addProduct(Scanner userInput, StockManager stockManager, ProductCatalog productCatalog) {
        Integer id = stockManager.generateId();
        String name = getProductName(userInput);
        String description = getProductDescription(userInput);
        Float price = getProductPrice(userInput);
        Integer[] thresholds = getProductThresholds(userInput);
        Integer stock = getProductStock(userInput);
        String category = productCatalog.getValidCategory(userInput);
        List<String> additionalParameters = getAdditionalParameters(userInput);

        Product newProduct = new Product(id, name, description, additionalParameters, price, thresholds[0], thresholds[1], stock, category);
        stockManager.addProduct(newProduct);
        System.out.println("Product " + newProduct.getName() + " added successfully.");
    }

    private static void editProduct(Scanner userInput, StockManager stockManager, ProductCatalog productCatalog) {
        Product product = getProductByIdOrName(userInput, stockManager);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Editing product: " + product.getName());
        System.out.println("Select attribute to edit (name, description, price, stock, category, thresholds, additional parameters): ");
        String attribute = userInput.nextLine();

        switch (attribute.toLowerCase()) {
            case "name":
                product.setName(getProductName(userInput));
                break;

            case "description":
                product.setDescription(getProductDescription(userInput));
                break;

            case "price":
                product.setPrice(getProductPrice(userInput));
                break;

            case "category":
                product.setCategory(productCatalog.getValidCategory(userInput));
                break;

            case "thresholds":
                Integer[] thresholds = getProductThresholds(userInput);
                product.setStockThresholds(thresholds);
                break;

            case "additional parameters":
                product.setAdditionalParameters(getAdditionalParameters(userInput));
                break;

            default:
                System.out.println("Invalid attribute.");
                return;
        }

        System.out.println("Product updated successfully.");
    }

    private static String getProductName(Scanner userInput) {
        System.out.println("Enter product name: ");
        return userInput.nextLine();
    }

    private static String getProductDescription(Scanner userInput) {
        System.out.println("Enter product description: ");
        return userInput.nextLine();
    }

    private static Float getProductPrice(Scanner userInput) {
        Float price = null;

        while (price == null || price < 0.1f) {

            System.out.println("Enter product price (must be at least 0.1): ");
            price = Float.parseFloat(userInput.nextLine());
            if (price < 0.1f) {
                System.out.println("Invalid price. Please enter a value of at least 0.1.");
            }
        }
        return price;
    }

    private static Integer[] getProductThresholds(Scanner userInput) {
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
        return new Integer[]{lowerThreshold, upperThreshold};
    }

    private static Integer getProductStock(Scanner userInput) {
        Integer stock = 0;
        while (stock <= 0) {
            System.out.println("Enter initial stock: ");
            stock = Integer.parseInt(userInput.nextLine());
            
            if (stock <= 0) {
                System.out.println("Invalid stock. Please enter a value higher than 0.");
            }
        }
        return stock;
    }

    private static List<String> getAdditionalParameters(Scanner userInput) {
        List<String> additionalParameters = new ArrayList<>();
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
        return additionalParameters;
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
    
        System.out.println("Do you want to increase or decrease product stock? (increase/decrease): ");
        String command = userInput.nextLine();
    
        if (!command.equalsIgnoreCase("increase") && !command.equalsIgnoreCase("decrease")) {
            System.out.println("Invalid command. Please enter 'increase' or 'decrease'.");
            return;
        }
    
        Integer quantity = null;
        while (quantity == null || quantity <= 0) {
            System.out.println("Enter quantity: ");
            String quantityInput = userInput.nextLine();
            
            try {
                quantity = Integer.parseInt(quantityInput);
                if (quantity <= 0) {
                    System.out.println("Invalid quantity value. Enter a value higher than 0.");
                }
            } 
            catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a numeric value.");
            }
        }
    
        if (command.equalsIgnoreCase("increase")) {
            stockManager.increaseStock(product.getProductId(), quantity);
        } 
        else if (command.equalsIgnoreCase("decrease")) {
            stockManager.decreaseStock(product.getProductId(), quantity);
        }
    
        System.out.println(product.getName() + " stock has been modified: " + oldStock + " -> " + product.getStock());
    }

    public static Product getProductByIdOrName(Scanner userInput, StockManager stockManager) {

        System.out.println("Enter product ID or name: ");
        String productIdentifier = userInput.nextLine();
        Product product = null;

        try {
            Integer productId = Integer.parseInt(productIdentifier);
            product = stockManager.getProductById(productId);

        } 
        catch (NumberFormatException e) {
            product = stockManager.getProductByName(productIdentifier);
        }

        return product;
    }

    public static void viewCatalog(Scanner userInput, ProductCatalog productCatalog, StockManager stockManager) {

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

    private static void manageOrders(Scanner userInput, StockManager stockManager, OrderManager orderManager) {
        System.out.println("Enter 'create', 'edit', 'delete', or 'view': ");
        String orderCommand = userInput.nextLine();

        switch(orderCommand.toLowerCase()) {
            case "create":
                createOrder(userInput, stockManager, orderManager);
                break;

            case "edit":
                editOrder(userInput, stockManager, orderManager);
                break;

            case "delete":
                deleteOrder(userInput, orderManager, stockManager);
                break;

            case "view":
                viewOrders(userInput, orderManager);
                break;

            default:
                System.out.println("Invalid command. Please enter 'create', 'edit', 'delete', or 'view'.");
        }
    }

    private static void createOrder(Scanner userInput, StockManager stockManager, OrderManager orderManager) {
        Integer orderId = orderManager.generateOrderId();
        Order newOrder = new Order(orderId);

        while (true) {
            String input = "";

            Product product = getProductByIdOrName(userInput, stockManager);
            if (product != null) {

                Integer quantity = 0;
                Integer productStock = product.getStock();
                while(quantity <= 0 || quantity > productStock) {

                    try {
                        System.out.println("Enter quantity: ");
                        quantity = Integer.parseInt(userInput.nextLine());
                    } 
                    catch (NumberFormatException e) {
                        System.out.println(quantity + " is not a number. Enter a valid value.");
                        continue;   
                    }
                    
                    if(quantity <= 0 || quantity > productStock) {
                        System.out.println("Invalid quantity. Value must be bigger than 0 and lower than " + productStock + ".");
                    }
                }
                Product orderProduct = new Product(product);

                orderProduct.setStock(quantity);
                newOrder.addProduct(orderProduct, quantity);
                stockManager.decreaseStock(product.getProductId(), quantity);
            } 
            else {
                System.out.println("Product not found.");
            }
            System.out.println("Type 'done' to finish or press enter to continue.");
            input = userInput.nextLine();

            if (input.equalsIgnoreCase("done") && newOrder.getProducts().size() != 0) {
                break;
            }
        }
        orderManager.createOrder(newOrder);
        System.out.println("Order created successfully.");
    }

    private static void editOrder(Scanner userInput, StockManager stockManager, OrderManager orderManager) {
        System.out.println("Enter the ID of the order to edit: ");
        Integer orderId = 0;

        try {
            orderId = Integer.parseInt(userInput.nextLine());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid value. Enter an ID.");
            return;
        }

        Order order = orderManager.getPendingOrders().get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        while (true) {
            System.out.println("Enter 'add' to add products, 'remove' to remove products, 'status' to change status, or 'done' to finish editing: ");
            String editCommand = userInput.nextLine();

            if (editCommand.equalsIgnoreCase("done")) {
                break;
            }

            switch (editCommand.toLowerCase()) {
                case "add":
                    addProductsToOrder(userInput, stockManager, order);
                    break;

                case "remove":
                    removeProductsFromOrder(userInput, order, stockManager);
                    break;

                case "status":
                    changeOrderStatus(userInput, orderManager, order);
                    break;

                default:
                    System.out.println("Invalid command. Please enter 'add', 'remove', 'status', or 'done'.");
            }
        }
    }

    private static void addProductsToOrder(Scanner userInput, StockManager stockManager, Order order) {
        Product product = getProductByIdOrName(userInput, stockManager);
        if (product != null) {

            Integer quantity = 0;
            Integer productStock = product.getStock();
            while (quantity <= 0 || quantity > productStock) {
                System.out.println("Enter quantity: ");
                quantity = Integer.parseInt(userInput.nextLine());
                
                if (quantity <= 0 || quantity > productStock) {
                    System.out.println("Invalid quantity. Value must be bigger than 0 and lower than " + productStock + ".");
                }
            }
            order.addProduct(new Product(product), quantity);
            stockManager.decreaseStock(product.getProductId(), quantity);

        } else {
            System.out.println("Product not found.");
        }
    }
    
    private static void removeProductsFromOrder(Scanner userInput, Order order, StockManager stockManager) {
        System.out.println("Enter product name to remove from the order: ");
        String input = userInput.nextLine();

        Product product = order.getProductByName(input);
        if (product != null) {
            order.removeProduct(product);
            stockManager.increaseStock(product.getProductId(), product.getStock());
            System.out.println("Product removed from order.");

        }
        else {
            System.out.println("Product not found in the order.");
        }
    }

    private static void changeOrderStatus(Scanner userInput, OrderManager orderManager, Order order) {
        System.out.println("Enter new status (PROCESSING, PACKAGING, SHIPPING, COMPLETED): ");
        String statusInput = userInput.nextLine();

        try {
            OrderStatus newStatus = OrderStatus.valueOf(statusInput.toUpperCase());

            orderManager.updateOrderStatus(order, newStatus);
            System.out.println("Order status updated successfully.");
        } 
        catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter a valid status.");
        }
    }

    private static void deleteOrder(Scanner userInput, OrderManager orderManager, StockManager stockManager) {
        System.out.println("Enter the ID of the order to delete: ");
        Integer orderId = Integer.parseInt(userInput.nextLine());

        orderManager.removePendingOrderById(orderId, stockManager);
    }

    private static void viewOrders(Scanner userInput, OrderManager orderManager) {
        System.out.println("Enter the ID of the order to view details or 'all' to view all pending orders: ");
        String input = userInput.nextLine();

        if (input.equalsIgnoreCase("all")) {
            orderManager.listPendingOrders();
        } 
        else {
            try {
                Integer orderId = Integer.parseInt(input);
                orderManager.printPendingOrderById(orderId);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid order ID or 'all'.");
            }
        }
    }

    public static void saveConfig(ConfigReader configReader, String configFilePath, ProductCatalog productCatalog, StockManager stockManager) {
        configReader.saveConfig(configFilePath, productCatalog, stockManager);
    }

    public static void loadConfig(ConfigReader configReader, String filePath, ProductCatalog productCatalog, StockManager stockManager) {
        configReader.loadConfig(filePath, productCatalog, stockManager);
    }

    public static void editFilePaths(Scanner userInput, String configFilePath, String orderLogFilePath) {
        System.out.println("Do you want to edit the config or order log path? (config/order) ");
        String pathCommand = userInput.nextLine();

        if (pathCommand.equalsIgnoreCase("config")) {

            System.out.println("Enter new path: ");
            configFilePath = userInput.nextLine();
        }
        else if (pathCommand.equalsIgnoreCase("order")) {

            System.out.println("Enter new path: ");
            orderLogFilePath = userInput.nextLine();
        }
        else {
            System.out.println("Invalid command.");
            return;
        }

        System.out.println("<><><><><><>");
        System.out.println("New path saved. Current paths: ");
        System.out.println(configFilePath);
        System.out.println(orderLogFilePath);
        System.out.println("<><><><><><>");
    }
}
