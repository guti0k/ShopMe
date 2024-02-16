package com.shopme.admin.brand;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.security.exception.BrandNotFoundException;
import com.shopme.admin.security.exception.CategoryNotFoundException;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class BrandController {

    @Autowired
    public BrandService brandService;

    @Autowired
    public CategoryService categoryService;

    @GetMapping(value = "/brands")
    public String listFirstPage(Model model) {
        return listByPage(1, "name", "asc", null, model);
    }

    @GetMapping(value = "/brands/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") Integer pageNumber, @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
        if(sortDir.isEmpty() || sortDir == null) {
            sortDir = "asc";
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<Brand> pageBrand = brandService.listByPage(keyword, sortField, sortDir, pageNumber);

        long startCount = (pageNumber - 1) * BrandService.BRANDS_PER_PAGE + 1;
        long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
        endCount = endCount > pageBrand.getTotalElements() ? pageBrand.getTotalElements() : endCount;

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageBrand.getTotalElements());
        model.addAttribute("totalPages", pageBrand.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("listBrands", pageBrand.getContent());

        return "brands/brands";
    }

    @GetMapping(value = "/brands/new")
    public String newBrand(Model model) {
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();

        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");
        model.addAttribute("listCategories", categoryList);

        return "brands/brand_form";
    }

    @GetMapping(value = "/brands/edit/{id}")
    public String editBrand(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            List<Category> categoryList = categoryService.listCategoriesUsedInForm();
            Brand brand = brandService.getBandById(id);

            model.addAttribute("brand", brand);
            model.addAttribute("pageTitle", "Edit Brand (ID : " + id + ")");
            model.addAttribute("listCategories", categoryList);

            return "brands/brand_form";
        }
        catch (BrandNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/brands";
        }
    }

    @PostMapping(value = "/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand brandSave = brandService.save(brand);

            String uploadDir = "ShopmeWebParent/brand-logos/" + brandSave.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");
        return "redirect:/brands";
    }

    @GetMapping(value = "/brands/delete/{id}")
    public String deleteBrand(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            brandService.deleteBrandById(id);
            String brandDir = "ShopmeWebParent/brand-logos/" + id;
            FileUploadUtil.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");
        }
        catch (BrandNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/brands";
    }

}
