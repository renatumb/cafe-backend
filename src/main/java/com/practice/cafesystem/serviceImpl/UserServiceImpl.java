package com.practice.cafesystem.serviceImpl;

import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.dao.UserDAO;
import com.practice.cafesystem.jwt.CustomerUsersDetailsService;
import com.practice.cafesystem.jwt.JwtUtils;
import com.practice.cafesystem.pojo.User;
import com.practice.cafesystem.service.UserService;
import com.practice.cafesystem.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("UserServiceImpl.signUp()", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDAO.findByEmailId(requestMap.get("email"));

                if (Objects.isNull(user)) {
                    userDAO.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully created", HttpStatus.CREATED);
                } else {
                    return CafeUtils.getResponseEntity("Email already Exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("UserServiceImpl.login()", requestMap);
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if (auth.isAuthenticated()) {
                var user = customerUsersDetailsService.getUser();
                if ( user != null &&  Boolean.parseBoolean( user.getStatus() ) ) {
                    String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
                    return new ResponseEntity<String>("{\"token:\"" + "\"" + token + "\"}", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("Wait for Admin Approval", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("==>> ", e);
        }
        return CafeUtils.getResponseEntity("BAD Credentials", HttpStatus.BAD_REQUEST);
    }
}
