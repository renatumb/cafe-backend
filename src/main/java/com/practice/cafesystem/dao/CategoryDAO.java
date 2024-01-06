package com.practice.cafesystem.dao;

import com.practice.cafesystem.pojo.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {

    List<Category> findAllforProduct();
}
