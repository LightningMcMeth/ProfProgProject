import java.util.ArrayList;
import java.util.List;

public class Product {
    
    private Integer productId;
    private String name;
    private String description;
    private Float price;
    private Integer[] stockThresholds;
    private Integer stock;
    private List<String> additionalParameters;
    private String category;
    private String stockLevel;

    public Product(Integer prodId, String name, String desc, List<String> additionalParams, Float price,Integer lowerBound, Integer upperBound, Integer stock, String category) {
        this.productId = prodId;
        this.name = name;
        this.description = desc;
        this.price = price;
        this.stockThresholds = new Integer[2];
        this.stockThresholds[0] = lowerBound;
        this.stockThresholds[1] = upperBound;
        this.stock = stock;
        this.additionalParameters = additionalParams;
        this.category = category;
    }

    public Product(Product other) {
        this.productId = other.productId;
        this.name = other.name;
        this.description = other.description;
        this.additionalParameters = new ArrayList<>(other.additionalParameters);
        this.price = other.price;
        this.stockThresholds = new Integer[2];
        this.stockThresholds[0] = other.stockThresholds[0];
        this.stockThresholds[1] = other.stockThresholds[1];
        this.stock = other.stock;
        this.category = other.category;
        this.stockLevel = other.stockLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer newStock) {
        this.stock = newStock;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer newProductId) {
        this.productId = newProductId;
    }

    public Integer[] getStockThresholds() {
        return stockThresholds;
    }

    public void setStockThresholds(Integer[] newStockThresholds) {
        this.stockThresholds = newStockThresholds;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float newPrice) {
        this.price = newPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public List<String> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(List<String> newAdditionalParameters) {
        this.additionalParameters = newAdditionalParameters;
    }

    public Integer getLowerThreshold() {
        return stockThresholds[0];
    }

    public void setLowerThreshold(Integer newLowerThreshold) {
        this.stockThresholds[0] = newLowerThreshold;
    }

    public Integer getUpperThreshold() {
        return stockThresholds[1];
    }

    public void setUpperThreshold(Integer newUpperThreshold) {
        this.stockThresholds[1] = newUpperThreshold;
    }

    public String getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(String stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String newCategory) {
        this.category = newCategory;
    }
}
