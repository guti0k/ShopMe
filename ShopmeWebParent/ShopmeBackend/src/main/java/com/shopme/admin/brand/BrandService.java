package com.shopme.admin.brand;

import com.shopme.admin.security.exception.BrandNotFoundException;
import com.shopme.admin.security.exception.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    public static final int BRANDS_PER_PAGE = 5;
    @Autowired
    public BrandRepository brandRepository;

    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    public Page<Brand> listByPage(String keyword, String sortField, String sortDir, int pageNumber) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, BRANDS_PER_PAGE, sort);

        if(keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }

        return brandRepository.findAll(pageable);
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrandById(Integer id) throws BrandNotFoundException {
        long countById = brandRepository.countById(id);

        if (countById == 0) {
            throw new BrandNotFoundException("Could not find any category with ID " + id);
        }
        brandRepository.deleteById(id);
    }

    public Brand getBandById(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        }
        catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = id == null || id == 0;
        Brand brandByName = brandRepository.findByName(name);

        if(isCreatingNew) {
            if(brandByName != null) {
                return "Duplicate";
            }
        }
        else {
            if(brandByName != null && brandByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }
}
