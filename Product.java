import java.util.List;

public class Product {
    
    private Integer productId;
    private String name;
    private String description;
    private Float price;
    private Integer[] stockThresholds;
    private Integer stock;
    private List<String> additionalParameters;

    public Product(Integer prodId, String name, String desc, List<String> additionalParams, Float price,Integer lowerBound, Integer upperBound, Integer stock) {
        this.productId = prodId;
        this.name = name;
        this.description = desc;
        this.price = price;
        this.stockThresholds = new Integer[2];
        this.stockThresholds[0] = lowerBound;
        this.stockThresholds[1] = upperBound;
        this.stock = stock;
        this.additionalParameters = additionalParams;
    }

    public String getName() {
        return name;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer[] getStockThresholds() {
        return stockThresholds;
    }

    public Float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getLowerThreshold() {
        return stockThresholds[0];
    }

    public Integer getUpperThreshold() {
        return stockThresholds[1];
    }
}
