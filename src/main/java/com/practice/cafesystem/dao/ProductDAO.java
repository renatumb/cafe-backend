package com.practice.cafesystem.dao;

import com.practice.cafesystem.pojo.Product;
import com.practice.cafesystem.wrapper.ProductWrapper;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    List<ProductWrapper> getAllProducts();
}
