package com.practice.cafesystem.rest;

import com.practice.cafesystem.wrapper.UserWrapper;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/api/user")
public interface UserRest {

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path= "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path="/get")
    public ResponseEntity<List<UserWrapper>> getAllUsers();

    @PostMapping(path="/update")
    ResponseEntity<String> updateStatus(@RequestBody(required = true) Map <String, String > requestMap);

    @GetMapping("/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping("/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);

    @PostMapping("/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap);

}
