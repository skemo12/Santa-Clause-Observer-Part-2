package santa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import enums.Category;

/**
 * Class that stores the data of a Gift, implements comparable for sorting by
 * price.
 */
public final class Gift implements Comparable<Gift> {

    private String productName;
    private Double price;
    private Category category;
    @JsonIgnore
    private Integer quantity;
    @JsonIgnore
    private Integer orders;

    public Gift(final String productName, final Double price,
                final Category category, final Integer quantity) {
        this.setProductName(productName);
        this.setPrice(price);
        this.setCategory(category);
        this.setQuantity(quantity);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    @Override
    public int compareTo(final Gift o) {
        return (int) (getPrice() - o.getPrice());
    }
}
