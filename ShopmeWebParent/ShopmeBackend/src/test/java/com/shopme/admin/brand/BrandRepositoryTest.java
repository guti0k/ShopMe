package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    public BrandRepository brandRepository;

    @Test
    public void testCreateBrand() {
        Category categoryLaptop = new Category(1);
        Category categoryMobile = new Category(2);
        Category categoryPC = new Category(3);
        Category categoryEarphone = new Category(39);
        Category categoryTivi = new Category(37);
        Category categoryTablet = new Category(38);

        Brand brandAsus = new Brand("Asus");
        brandAsus.getCategories().add(categoryLaptop);
        brandAsus.getCategories().add(categoryPC);

        Brand brandLenovo = new Brand("Lenovo");
        brandLenovo.getCategories().add(categoryLaptop);

        Brand brandMSI = new Brand("MSI");
        brandMSI.getCategories().add(categoryLaptop);

        Brand brandIphone = new Brand("Apple");
        brandIphone.getCategories().add(categoryMobile);
        brandIphone.getCategories().add(categoryEarphone);
        brandIphone.getCategories().add(categoryLaptop);
        brandIphone.getCategories().add(categoryTablet);

        Brand brandSamsung = new Brand("Samsung");
        brandSamsung.getCategories().add(categoryMobile);
        brandSamsung.getCategories().add(categoryEarphone);
        brandSamsung.getCategories().add(categoryTivi);

        Brand brandXiaomi = new Brand("Xiaomi");
        brandXiaomi.getCategories().add(categoryMobile);
        brandSamsung.getCategories().add(categoryEarphone);

        brandRepository.saveAll(List.of(brandAsus, brandLenovo, brandMSI, brandSamsung, brandIphone, brandXiaomi));
    }
}
