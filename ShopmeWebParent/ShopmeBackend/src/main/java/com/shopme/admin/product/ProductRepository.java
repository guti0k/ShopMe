package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Product findProductByName(String name);

    @Query("update Product p set p.enabled = ?2 where p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    public long countProductById(Integer id);
}
