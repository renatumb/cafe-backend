package com.practice.cafesystem.service;

import com.practice.cafesystem.wrapper.ProductWrapper;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<String> addProduct(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProducts();

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    ResponseEntity<String> updateProductStatus(Map<String, String> requestMap);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id);

    ResponseEntity<ProductWrapper> getProductById(Integer id);
}
