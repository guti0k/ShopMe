package com.shopme.admin.category;

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
public class CategoryRepoTest {
    @Autowired
    public CategoryRepository categoryRepo;

    @Test
    public void testCreateRootCategory() {
        Category categoryLap = new Category("Laptop");
        Category categoryPC = new Category("PC");
        Category categoryMobile = new Category("Mobile Phone");
        categoryRepo.saveAll(List.of(categoryLap, categoryMobile, categoryPC));
    }

    @Test
    public void testCreateSubCategory() {
        Category categoryLap = new Category(1);
        Category categoryMobile = new Category(2);

        Category categoryAsus =  new Category("Laptop Asus", categoryLap);
        Category categoryLenovo =  new Category("Laptop Lenovo", categoryLap);
        Category categoryDell =  new Category("Laptop Dell", categoryLap);

        Category categoryIphone =  new Category("Iphone", categoryMobile);
        Category categorySamsung =  new Category("Samsung", categoryMobile);
        Category categoryXiaomi =  new Category("Xiaomi", categoryMobile);

        categoryRepo.saveAll(List.of(categoryAsus, categoryLenovo, categoryDell, categoryIphone, categorySamsung, categoryXiaomi));

        Category categoryAsus1 =  new Category("Laptop Asus Gaming ROG Zephyrus", categoryAsus);
        Category categoryAsus2 =  new Category("Laptop Asus Gaming ROG Strix", categoryAsus);
        Category categoryAsus3 =  new Category("Laptop Asus Zenbook", categoryAsus);

        Category categoryLenovo1 =  new Category("Laptop Lenovo Gaming Legion", categoryLenovo);
        Category categoryLenovo2 =  new Category("Laptop Lenovo Ideapad", categoryLenovo);
        Category categoryLenovo3 =  new Category("Laptop Lenovo Thinkpad", categoryLenovo);

        Category categoryDell1 =  new Category("Laptop Dell Gaming", categoryDell);
        Category categoryDell2 =  new Category("Laptop Dell Inspiron", categoryDell);
        Category categoryDell3 =  new Category("Laptop Dell Vostro", categoryDell);

        Category categoryIphone1 =  new Category("IPhone 12PRM", categoryIphone);
        Category categoryIphone2 =  new Category("IPhone 13PRM", categoryIphone);
        Category categoryIphone3 =  new Category("IPhone 14PRM", categoryIphone);

        categoryRepo.saveAll(List.of(categoryAsus1, categoryAsus2, categoryAsus3,
                categoryLenovo1, categoryLenovo2, categoryLenovo3,
                categoryDell1, categoryDell2, categoryDell3,
                categoryIphone1, categoryIphone2, categoryIphone3));
    }

}
