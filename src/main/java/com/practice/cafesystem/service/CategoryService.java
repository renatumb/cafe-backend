package com.practice.cafesystem.service;

import com.practice.cafesystem.pojo.Category;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategories(String filter);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
