package com.shopme.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Category> children = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.enabled = true;
        this.image = "image-thumbnail.png";
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }
    public static Category copyCategory(Category category) {
        Category categoryCopy = new Category();
        categoryCopy.setId(category.getId());
        categoryCopy.setName(category.getName());
        categoryCopy.setAlias(category.getAlias());
        categoryCopy.setImage(category.getImage());
        categoryCopy.setEnabled(category.isEnabled());
        categoryCopy.setHasChildren(category.getChildren().size() > 0);

        return categoryCopy;
    }

    public static Category copyCategory(Category category, String name) {
        Category categoryCopy = copyCategory(category);
        categoryCopy.setName(name);

        return categoryCopy;
    }

    public static Category copyIdAndName(Category category) {
        Category categoryCopy = new Category();
        categoryCopy.setId(category.getId());
        categoryCopy.setName(category.getName());

        return categoryCopy;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category categoryCopy = new Category();
        categoryCopy.setId(id);
        categoryCopy.setName(name);

        return categoryCopy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Transient
    public String getImagePath() {
        if(this.id == null) {
            return "/images/image-thumbnail.png";
        }
        return "/ShopmeWebParent/category-images/" + this.id + "/" + this.image;
    }

    @Transient
    public boolean hasChildren;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
