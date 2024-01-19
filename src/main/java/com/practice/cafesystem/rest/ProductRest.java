package com.practice.cafesystem.rest;

import com.practice.cafesystem.wrapper.ProductWrapper;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/product")
public interface ProductRest {

    @PostMapping("/add")
    ResponseEntity<String> addProduct(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    @GetMapping("/get/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable("id") Integer id);

    @GetMapping("/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getProductByCategory(@PathVariable("id") Integer id);

    @PostMapping("/update")
    ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/updateStatus")
    ResponseEntity<String> updateProductStatus(@RequestBody(required = true) Map<String, String> requestMap);


    @GetMapping("/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);
}
