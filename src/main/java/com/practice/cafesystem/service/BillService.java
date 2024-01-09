package com.practice.cafesystem.service;

import com.practice.cafesystem.pojo.Bill;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

    ResponseEntity<String> deleteBill(Integer id);
}
