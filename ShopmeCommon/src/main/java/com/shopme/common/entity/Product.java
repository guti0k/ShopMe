package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 256, nullable = false, unique = true)
    private String name;

    @Column(length = 256, nullable = false, unique = true)
    private String alias;

    @Column(nullable = false, length = 5000)
    private String shortDescription;

    @Column(nullable = false, length = 50000)
    private String fullDescription;

    private String mainImage;

    private Date createdTime;

    private Date updatedTime;

    private Boolean enabled;

    private Boolean inStock;

    private Double discountPercent;
    private Double price;

    private Double cost;

    private Double length;
    private Double width;
    private Double height;
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> details = new ArrayList<>();
    public void addExtraImage(String name) {
        this.images.add(new ProductImage(name, this));
    }

    public void addProductDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
    }
    @Transient
    public String mainImagePath() {
        if(id == null || mainImage == null) {
            return "/images/image-thumbnail.png";
        }
        return "/ShopmeWebParent/product-images/" + this.id + "/" + this.mainImage;
    }
}
