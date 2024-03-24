package com.practice.cafesystem.rest;


import com.practice.cafesystem.pojo.Bill;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/bill")
public interface BillRest {

    @PostMapping("/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);

    @GetMapping("/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping("/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);

    @GetMapping("/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);


}
