package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {

        if(product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if(product.getAlias() == null || product.getAlias().isEmpty()) {
            product.setAlias(product.getName().replaceAll(" ", "_"));
        }
        else {
            product.setAlias(product.getAlias().replaceAll(" ", "_"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreating = (id == null || id == 0);
        Product product = productRepository.findProductByName(name);

        if(isCreating) {
            if(product != null) {
                return "Duplicate";
            }
        }
        else {
            if(product.getId() != id && product != null) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, boolean status) {
        productRepository.updateEnabledStatus(id, status);
    }

    public void deleteProductById(Integer id) throws ProductNotFoundException {
        long countById = productRepository.countProductById(id);

        if(countById == 0) {
            throw new ProductNotFoundException("Could not found any product with ID : " + id);
        }

        productRepository.deleteById(id);
    }

    public Product getProductById(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        }
        catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with ID = " + id);
        }
    }
}
