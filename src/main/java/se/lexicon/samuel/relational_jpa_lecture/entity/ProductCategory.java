package se.lexicon.samuel.relational_jpa_lecture.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
@Entity
public class ProductCategory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String categoryId;
    @Column(unique = true)
    private String value;
    //a set here makes it possible to browse through the products before picking
    //take note the product is a set here (so bi-directional between the product and the product category)
    // and it is pointed to it as such here
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "product_product_category",
            joinColumns = @JoinColumn(name = "category_id"), // product table name to be joined with the product
            inverseJoinColumns = @JoinColumn(name = "product_id") //joining with the product
    )
    private Set<Product> products;

    public ProductCategory(String categoryId, String value, Set<Product> products) {
        this.categoryId = categoryId;
        this.value = value;
        this.products = products;
    }

    public ProductCategory() {

    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, value);
    }
}
