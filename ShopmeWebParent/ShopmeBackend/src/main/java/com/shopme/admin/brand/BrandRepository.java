package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    public long countById(Integer id);
    public Brand findByName(String name);

    @Query("select b from Brand b where b.name like %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);

    @Query("select new Brand(b.id, b.name) from Brand b order by b.name asc")
    public List<Brand> findAll();
}
