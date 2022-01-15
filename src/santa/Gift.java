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

    public Gift(final String productName, final Double price,
                final Category category) {
        this.setProductName(productName);
        this.setPrice(price);
        this.setCategory(category);
    }
    public static final class Builder {
        private String productName;
        private Double price;
        private Category category;
        @JsonIgnore
        private Integer quantity;

        public Builder(final String productName, final Double price,
                       final Category category) {
            this.productName = productName;
            this.price = price;
            this.category = category;
        }

        /**
         * Sets elf from builder
         */
        public Builder quantity(final Integer quantitySet) {
            this.quantity = quantitySet;
            return this;
        }
        /**
         * build method for Builder
         */
        public Gift build() {
            return new Gift(this);
        }
    }

    private Gift(final Builder builder) {
        this.productName = builder.productName;
        this.price = builder.price;
        this.category = builder.category;
        this.quantity = builder.quantity;

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

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(final Gift o) {
        return (int) (getPrice() - o.getPrice());
    }
}
