package com.shopme.admin.category;

import com.shopme.admin.export.CategoryCsvExporter;
import com.shopme.admin.security.exception.CategoryNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(Model model) {
        return listByPage(1, "asc", null , model);
    }
    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") Integer pageNumber, @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {
        if(sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        String sortField = "name";

        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
        List<Category> categoryList = categoryService.listByPage(categoryPageInfo, pageNumber, sortDir, keyword, sortField);

        int startCount = (pageNumber - 1) * categoryService.ROOT_CATEGORIES_PER_PAGE  + 1;
        long endCount = startCount + categoryService.ROOT_CATEGORIES_PER_PAGE - 1;
        endCount = endCount > categoryPageInfo.getTotalItems() ? categoryPageInfo.getTotalItems() : endCount;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
        model.addAttribute("totalItems", categoryPageInfo.getTotalItems());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("listCategories", categoryList);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category saveCategory = categoryService.save(category);

            String uploadDir = "ShopmeWebParent/category-images/" + saveCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.findCatetoryById(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID : " + category.getId() + ")");

            return "categories/category_form";
        }
        catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes redirectAttributes) {

        categoryService.updateCategoryEnabledStatus(id, status);

        String enabled = status ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + enabled;

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            categoryService.deleteCategory(id);
            String categoryDir = "ShopmeWebParent/category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
        }
        catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void deleteCategory(HttpServletResponse httpServletResponse) throws IOException {
        List<Category> categoryListInForm = categoryService.listCategoriesUsedInForm();
        CategoryCsvExporter categoryCsvExporter = new CategoryCsvExporter();
        categoryCsvExporter.export(categoryListInForm, httpServletResponse);
    }
}
