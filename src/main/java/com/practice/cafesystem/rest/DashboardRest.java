package com.practice.cafesystem.rest;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/dashboard")
public interface DashboardRest {

    @GetMapping("/details")
    ResponseEntity<Map<String, Object>> getDetails();
}
