package com.practice.cafesystem.restImpl;

import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.rest.UserRest;
import com.practice.cafesystem.service.UserService;
import com.practice.cafesystem.utils.CafeUtils;
import com.practice.cafesystem.wrapper.UserWrapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return userService.updateStatus(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            return userService.changePassword(requestMap);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return userService.forgotPassword( requestMap);
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
