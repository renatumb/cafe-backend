package com.practice.cafesystem.serviceImpl;

import com.practice.cafesystem.dao.BillDAO;
import com.practice.cafesystem.dao.CategoryDAO;
import com.practice.cafesystem.dao.ProductDAO;
import com.practice.cafesystem.service.DashboardService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    BillDAO billDAO;

    @Override
    public ResponseEntity<Map<String, Object>> getDetails() {
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("category", categoryDAO.count());
        hashMap.put("product", productDAO.count());
        hashMap.put("bill", billDAO.count());

        return new ResponseEntity(hashMap, HttpStatus.OK);
    }
}
