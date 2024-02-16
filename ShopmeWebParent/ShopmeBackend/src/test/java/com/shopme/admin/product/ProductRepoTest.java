package com.shopme.admin.product;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepoTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveProductWithImages() {
        Product product = productRepository.findById(14).get();
        product.setMainImage("main_image.jpg");

        product.addExtraImage("extra_image1.jpg");
        product.addExtraImage("extra_image2.jpg");
        product.addExtraImage("extra_image3.jpg");

        productRepository.save(product);
    }

    @Test
    public void testCreateProduct() {
        Brand brandApple = entityManager.find(Brand.class, 17);
        Category categoryMobile = entityManager.find(Category.class, 2);

        Product iphone12prm = new Product();
        iphone12prm.setName("Iphone 12PRM");
        iphone12prm.setAlias("12PRM");
        iphone12prm.setShortDescription("iPhone 12 Pro Max một siêu phẩm smartphone đến từ Apple.");
        iphone12prm.setFullDescription("“Trùm cuối” của dòng iPhone 12 đã xuất hiện. iPhone 12 Pro Max là chiếc iPhone có màn hình lớn nhất từ trước đến nay, mang trên mình bộ vi xử lý mạnh nhất, camera đẳng cấp pro cùng kết nối 5G siêu tốc, cho bạn những trải nghiệm tuyệt vời chưa từng có.");

        iphone12prm.setBrand(brandApple);
        iphone12prm.setCategory(categoryMobile);

        iphone12prm.setPrice(15000000.0);
        iphone12prm.setCost(12000000.0);
        iphone12prm.setEnabled(true);
        iphone12prm.setInStock(true);
        iphone12prm.setCreatedTime(new Date());
        iphone12prm.setUpdatedTime(new Date());

        Product iphone13prm = new Product();
        iphone13prm.setName("Iphone 13PRM");
        iphone13prm.setAlias("13PRM");
        iphone13prm.setShortDescription("iPhone 13 Pro Max một siêu phẩm smartphone đến từ Apple.");
        iphone13prm.setFullDescription("“Trùm cuối” của dòng iPhone 13 đã xuất hiện. iPhone 13 Pro Max là chiếc iPhone có màn hình lớn nhất từ trước đến nay, mang trên mình bộ vi xử lý mạnh nhất, camera đẳng cấp pro cùng kết nối 5G siêu tốc, cho bạn những trải nghiệm tuyệt vời chưa từng có.");

        iphone13prm.setBrand(brandApple);
        iphone13prm.setCategory(categoryMobile);

        iphone13prm.setPrice(20000000.0);
        iphone13prm.setCost(16000000.0);
        iphone13prm.setEnabled(true);
        iphone13prm.setInStock(true);
        iphone13prm.setCreatedTime(new Date());
        iphone13prm.setUpdatedTime(new Date());

        Product iphone14prm = new Product();
        iphone14prm.setName("Iphone 14PRM");
        iphone14prm.setAlias("14PRM");
        iphone14prm.setShortDescription("iPhone 14 Pro Max một siêu phẩm smartphone đến từ Apple.");
        iphone14prm.setFullDescription("“Trùm cuối” của dòng iPhone 14 đã xuất hiện. iPhone 14 Pro Max là chiếc iPhone có màn hình lớn nhất từ trước đến nay, mang trên mình bộ vi xử lý mạnh nhất, camera đẳng cấp pro cùng kết nối 5G siêu tốc, cho bạn những trải nghiệm tuyệt vời chưa từng có.");

        iphone14prm.setBrand(brandApple);
        iphone14prm.setCategory(categoryMobile);

        iphone14prm.setPrice(25000000.0);
        iphone14prm.setCost(20000000.0);
        iphone14prm.setEnabled(true);
        iphone14prm.setInStock(true);
        iphone14prm.setCreatedTime(new Date());
        iphone14prm.setUpdatedTime(new Date());


        Brand brandAsus = entityManager.find(Brand.class, 13);
        Category categoryLaptop = entityManager.find(Category.class, 1);

        Product asusRogStrixG513 = new Product();
        asusRogStrixG513.setName("Laptop Gaming Asus ROG Strix G15 G513QC HN015T");
        asusRogStrixG513.setAlias("ROG Strix G15 G513QC HN015T");
        asusRogStrixG513.setShortDescription("Asus ROG Strix G513QC có thiết kế hiện đại và thanh lịch. Chiếc laptop này được làm từ chất liệu nhựa, có độ bền cao. Kích thước của Asus ROG Strix G513QC là 359 x 243 x 19.9 mm, trọng lượng 2.2 kg, khá gọn nhẹ và dễ dàng mang theo khi di chuyển.");
        asusRogStrixG513.setFullDescription("ASUS ROG Strix G15 là hiện thân của phong cách thiết kế tối giản, mang đến trải nghiệm cốt lõi mãnh liệt nhất. Dư sức để chiến những tựa game nặng kí và xử lý đa nhiệm cùng Windows 10. Hệ thống tản nhiệt thông minh giúp giải phóng sức mạnh của R7 5800H và RTX 3050.");

        asusRogStrixG513.setBrand(brandAsus);
        asusRogStrixG513.setCategory(categoryLaptop);

        asusRogStrixG513.setPrice(27000000.0);
        asusRogStrixG513.setCost(20000000.0);
        asusRogStrixG513.setEnabled(true);
        asusRogStrixG513.setInStock(true);
        asusRogStrixG513.setCreatedTime(new Date());
        asusRogStrixG513.setUpdatedTime(new Date());

        productRepository.saveAll(List.of(iphone12prm, iphone13prm, iphone14prm, asusRogStrixG513));

    }
}
