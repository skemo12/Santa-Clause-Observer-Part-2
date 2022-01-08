package santa;

import enums.Category;

/**
 * Class that stores the data of a Gift, implements comparable for sorting by
 * price.
 */
public final class Gift implements Comparable<Gift> {

    private String productName;
    private Double price;
    private Category category;

    public Gift(final String productName, final Double price,
                final Category category) {
        this.setProductName(productName);
        this.setPrice(price);
        this.setCategory(category);
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

    @Override
    public int compareTo(final Gift o) {
        return (int) (getPrice() - o.getPrice());
    }
}
