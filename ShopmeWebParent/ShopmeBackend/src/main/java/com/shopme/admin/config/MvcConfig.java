package com.shopme.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {

        Path pathDir = Paths.get(pathPattern);
//        đường dẫn chi tiết đến folder
        String absolutePath = pathDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + pathPattern + "/**")
                .addResourceLocations("file:/" + absolutePath + "/");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        exposeDirectory("user-photos", registry);

        exposeDirectory("ShopmeWebParent/category-images", registry);

        exposeDirectory("ShopmeWebParent/brand-logos", registry);

        exposeDirectory("ShopmeWebParent/product-images", registry);

    }

}
