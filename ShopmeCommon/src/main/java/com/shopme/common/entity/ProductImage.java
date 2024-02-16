package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public ProductImage(String name) {
        this.name = name;
    }

    @Transient
    public String extraImagePath() {
        if(this.id == null) {
            return "/images/image-thumbnail.png";
        }
        return "/ShopmeWebParent/product-images/" + product.getId() + "/extras/" + this.name;
    }
}
