package com.practice.cafesystem.serviceImpl;

import com.google.common.base.Strings;
import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.dao.CategoryDAO;
import com.practice.cafesystem.jwt.JwtMyFilter;
import com.practice.cafesystem.pojo.Category;
import com.practice.cafesystem.service.CategoryService;
import com.practice.cafesystem.utils.CafeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    JwtMyFilter jwtMyFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (jwtMyFilter.isAdmin()) {
                if (validateCategory(requestMap, false)) {
                    categoryDAO.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories(String filter) {
        try {
            if (!Strings.isNullOrEmpty(filter) && filter.equalsIgnoreCase("true")) {
                // Not implemented ??? why  ??
            }
            return new ResponseEntity<List<Category>>(categoryDAO.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtMyFilter.isAdmin()) {
                if (validateCategory(requestMap, true)) {
                    Optional optional = categoryDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        categoryDAO.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                    }
                    return CafeUtils.getResponseEntity("Category doesnt exist", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategory(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name") && requestMap.containsKey("id") && validateId) {
            return true;
        }
        if (requestMap.containsKey("name") && !requestMap.containsKey("id") && !validateId) {
            return true;
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> request, boolean isUpdate) {
        Category category = new Category();
        category.setName(request.get("name"));
        if (isUpdate) {
            category.setId(Integer.parseInt(request.get("id")));
        }
        return category;
    }
}
