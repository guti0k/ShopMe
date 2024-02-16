package com.shopme.admin.product;

import com.shopme.admin.brand.BrandRestController;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.CategoryDTO;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/products")
    public String listFistPage(Model model) {
        List<Product> listProducts = productService.listAll();

        model.addAttribute("listProducts", listProducts);

        return "products/product";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();

        Product product = new Product();
        product.setInStock(true);
        product.setEnabled(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberExtraImages", 0);

        return "products/product_form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) throws ProductNotFoundException {
        try {
            Product product = productService.getProductById(id);
            List<Brand> listBrands = brandService.listAll();

            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", "Edit Product (ID : " + id + " )");
            model.addAttribute("numberExtraImages", product.getImages().size());

            return "products/product_form";
        }
        catch (ProductNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam("fileImage") MultipartFile mainImageMultipart,
                              @RequestParam("extraImage") MultipartFile[] extraImageMultiparts,
                              @RequestParam("detailNames") String[] detailNames,
                              @RequestParam("detailValues") String[] detailValues) throws IOException {

//        setMainImageNames(mainImageMultipart, product);
//        setExtraImageNames(extraImageMultiparts, product);
//        setProductDetails(detailNames, detailValues, product);
//
//        Product productSave = productService.save(product);
//
//        saveUploadedImages(mainImageMultipart, extraImageMultiparts, product);

        redirectAttributes.addAttribute("message", "The product has been saved successfully");
        return "redirect:/products";
    }

    public void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if(detailNames.length > 0 || detailNames != null) {

            for (int i = 0; i < detailNames.length; i++) {
                if(!detailNames[i].isEmpty() && !detailValues[i].isEmpty()) {
                    product.addProductDetail(detailNames[i], detailValues[i]);
                }
            }
        }
    }
    private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product product) throws IOException {
        if(!mainImageMultipart.isEmpty()) {
            String uploadDir = "ShopmeWebParent/product-images/" + product.getId();
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if(extraImageMultiparts.length > 0) {
            String uploadDir = "ShopmeWebParent/product-images/" + product.getId() + "/extras";
            for (MultipartFile extraImage: extraImageMultiparts) {

                if(!extraImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                    FileUploadUtil.saveFile(uploadDir, fileName, extraImage);
                }
            }
        }
    }

    private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        if (extraImageMultiparts.length > 0) {
            for (MultipartFile extraImage : extraImageMultiparts) {
                if (!extraImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(extraImage.getOriginalFilename());
                    product.addExtraImage(fileName);
                }
            }
        }
    }

    private void setMainImageNames(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, status);

        String enabled = status ? "enabled" : "disabled";
        String message = "The product ID " + id + " has been " + enabled;

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            productService.deleteProductById(id);

            String productExtraImagesDir = "ShopmeWebParent/product-images/" + id + "/extras";
            String productImagesDir = "ShopmeWebParent/product-images/" + id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);

            redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/products";
    }
}
