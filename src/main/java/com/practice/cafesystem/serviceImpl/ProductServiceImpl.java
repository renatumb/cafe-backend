package com.practice.cafesystem.serviceImpl;

import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.dao.ProductDAO;
import com.practice.cafesystem.jwt.JwtMyFilter;
import com.practice.cafesystem.pojo.Category;
import com.practice.cafesystem.pojo.Product;
import com.practice.cafesystem.service.ProductService;
import com.practice.cafesystem.utils.CafeUtils;
import com.practice.cafesystem.wrapper.ProductWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    JwtMyFilter jwtMyFilter;

    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try {
            if (jwtMyFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productDAO.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product added Sucessfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try {
            return new ResponseEntity<List<ProductWrapper>>(productDAO.getAllProducts(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtMyFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> product = productDAO.findById(Integer.parseInt(requestMap.get("id")));

                    if (!product.isEmpty()) {
                        Product p = getProductFromMap(requestMap, true);
                        p.setStatus(product.get().getStatus());
                        productDAO.save(p);
                        return CafeUtils.getResponseEntity("Product UPDATED Sucessfully", HttpStatus.OK);
                    }
                    return CafeUtils.getResponseEntity("Product DOES not Exist", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        try {
            if (jwtMyFilter.isAdmin()) {
                Optional product = productDAO.findById(Integer.parseInt(requestMap.get("id")));
                if (!product.isEmpty()) {
                    productDAO.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("Prodcut Status UPDATED successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product DOES not Exist", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtMyFilter.isAdmin()) {
                Optional product = productDAO.findById(id);
                if (!product.isEmpty()) {
                    productDAO.deleteById(id);
                    return CafeUtils.getResponseEntity("Product DELETED successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product DOES not Exist", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDAO.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<ProductWrapper>(productDAO.getProductById(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {

        if (requestMap.containsKey("name") && requestMap.containsKey("id") && validateId) {
            return true;
        }
        if (requestMap.containsKey("name") && !validateId) {
            return true;
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isUpdate) {
        Product product = new Product();
        Category category = new Category();

        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        if (isUpdate) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName((requestMap.get("name")));
        product.setDesccription((requestMap.get("description")));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }
}
