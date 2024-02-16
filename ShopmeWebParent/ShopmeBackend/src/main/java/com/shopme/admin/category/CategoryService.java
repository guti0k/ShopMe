package com.shopme.admin.category;

import com.shopme.admin.security.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    public static final int ROOT_CATEGORIES_PER_PAGE = 4;
    public List<Category> listByPage(CategoryPageInfo categoryPageInfo, int pageNumber, String sortDir, String keyword, String sortField) {
        Sort sort = Sort.by(sortField);

        if(sortDir.equals("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, ROOT_CATEGORIES_PER_PAGE, sort);
        Page<Category> pageRootCategories = null;
        if (keyword == null || keyword.trim().isEmpty()) {
            pageRootCategories = categoryRepo.findRootCategories(pageable);
        }
        else {
            pageRootCategories = categoryRepo.search(keyword, pageable);
        }

        categoryPageInfo.setTotalPages(pageRootCategories.getTotalPages());
        categoryPageInfo.setTotalItems(pageRootCategories.getTotalElements());

        if (keyword != null) {
            if (!keyword.trim().isEmpty())
            {
                List<Category> categoryList = pageRootCategories.getContent();

                for (Category children: categoryList) {
                    children.setHasChildren(children.getChildren().size() > 0);
                }
                return categoryList;
            }
        }

        return listHierarchicalCategories(pageRootCategories.getContent(), sortDir);
    }

    public Set<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    public Set<Category> sortSubCategories(Set<Category> children, String sortDir) {
        Comparator<Category> comparator = new Comparator<Category>() {
            @Override
            public int compare(Category category1, Category category2) {
                if(sortDir.equals("asc")) {
                    return category1.getName().compareTo(category2.getName());
                }
                else {
                    return category2.getName().compareTo(category1.getName());
                }
            }
        };

        Set<Category> sortedChildren = new TreeSet<>(comparator);
        sortedChildren.addAll(children);

        return sortedChildren;
    }
    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory: rootCategories) {
            hierarchicalCategories.add(Category.copyCategory(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category childCategory: children) {
                String name = "++" + " " + childCategory.getName();
                hierarchicalCategories.add(Category.copyCategory(childCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, childCategory, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    public void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int level, String sortDir) {
        level = level + 1;

        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        for (Category childCategory: children) {
            String prefix = "";
            for (int i = 0; i < level; i++) {
                prefix += "++";
            }
            hierarchicalCategories.add(Category.copyCategory(childCategory, prefix + " " + childCategory.getName()));
            listSubHierarchicalCategories(hierarchicalCategories, childCategory, level, sortDir);
        }
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> listCategoryUsedInForm = new ArrayList<>();

        List<Category> categoryListInDB = categoryRepo.findRootCategories(Sort.by("name").ascending());

        for (Category category: categoryListInDB) {
            if(category.getParent() == null) {
                listCategoryUsedInForm.add(Category.copyIdAndName(category));
                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category cate: children) {
                    listCategoryUsedInForm.add(Category.copyIdAndName(cate.getId(),"--" + " " + cate.getName()));
                    listChilrent(listCategoryUsedInForm, cate, 1);
                }
            }
        }
        return listCategoryUsedInForm;
    }

    private void listChilrent(List<Category> listCategoryUsedInForm, Category parent, int subLevel) {

        int newLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category category: children) {
            String prefix = "";
            for (int i = 0; i < newLevel; i++) {
                prefix += "--";
            }

            listCategoryUsedInForm.add(Category.copyIdAndName(category.getId(), prefix + " " + category.getName()));
            listChilrent(listCategoryUsedInForm, category, newLevel);
        }
    }

    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    public Category findCatetoryById(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepo.findById(id).get();
        }
        catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID" + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = id == null;

        Category categoryByName = categoryRepo.findByName(name);
//        Category categoryByAlias = categoryRepo.findByAlias(alias);

        if (isCreatingNew) {
            if(categoryByName != null) {
                return "DuplicateName";
            }
//            if(categoryByAlias != null) {
//                return "DuplicateAlias";
//            }
        }
        else {
            if(categoryByName.getId() != id && categoryByName != null) {
                return "DuplicateName";
            }
//            if(categoryByAlias.getId() != id && categoryByAlias != null) {
//                return "DuplicateAlias";
//            }
        }

        return "OK";
    }

    public void updateCategoryEnabledStatus(Integer id, boolean status) {
        categoryRepo.upadateEnabledStatus(id, status);
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {

        Long countById = categoryRepo.countById(id);
        if(countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepo.deleteById(id);
    }
}
