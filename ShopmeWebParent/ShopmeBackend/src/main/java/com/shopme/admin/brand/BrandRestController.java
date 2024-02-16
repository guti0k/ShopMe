package com.shopme.admin.brand;

import com.shopme.admin.security.exception.BrandNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class BrandRestController {
    @Autowired
    public BrandService brandService;

    @PostMapping(value = "/brands/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
        return brandService.checkUnique(id, name);
    }

    @GetMapping(value = "/brands/{id}/categories")
    public List<CategoryDTO> getCategoriesByBrand(@PathVariable("id") Integer id) throws BrandNotFoundRestException {
        try
        {
            Brand brand = brandService.getBandById(id);

            return brand.getCategories().stream().map(category -> new CategoryDTO(category.getId(), category.getName())).collect(Collectors.toList());
        }
        catch (BrandNotFoundException ex) {
            throw new BrandNotFoundRestException();
        }
    }
}
