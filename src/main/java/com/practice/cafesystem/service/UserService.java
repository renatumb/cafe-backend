package com.practice.cafesystem.service;

import com.practice.cafesystem.wrapper.UserWrapper;
import java.util.List;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);
}
