package com.shopme.admin.security.exception;

import com.shopme.common.entity.Brand;

public class BrandNotFoundException extends Exception {
    public BrandNotFoundException(String message) {
        super(message);
    }
}
