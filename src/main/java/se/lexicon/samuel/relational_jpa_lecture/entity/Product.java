package se.lexicon.samuel.relational_jpa_lecture.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productId;
    private String productName;
    //important when dealing with money, double should be spared
    private BigDecimal productPrice;
    //take note the product category is a set here (so bi-directional between the product and the product category)
    // and it is pointed to it as such here
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "product_product_category", //share same name as they relate
            joinColumns = @JoinColumn(name = "product_id"), //product table name to be joined with prod category
            inverseJoinColumns = @JoinColumn(name = "category_id") //table to be joined with
    )
    private Set<ProductCategory> categories;

    public Product(String productId, String productName, BigDecimal productPrice, Set<ProductCategory> categories) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.categories = categories;
    }

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Set<ProductCategory> getCategories() {
        if(this.categories == null) return new HashSet<>();
        return categories;
    }

    public void setCategories(Set<ProductCategory> categories) {
        this.categories = categories;
    }

    public void addCategories(ProductCategory...categories){
        if(this.categories == null) this.categories = new HashSet<>();
        if(categories != null){
            for(ProductCategory category : categories){
                if(this.categories.add(category)){
                    category.getProducts().add(this);
                }
            }
        }
    }

    public void removeCategories(ProductCategory...categories){
        if(this.categories == null) this.categories = new HashSet<>();
        if(categories != null){
            for(ProductCategory category : categories){
                if(this.categories.remove(category)){
                    category.getProducts().remove(this);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) && Objects.equals(productName, product.productName) && Objects.equals(productPrice, product.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productPrice);
    }


}
