package com.practice.cafesystem.serviceImpl;

import com.google.common.base.Strings;
import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.dao.UserDAO;
import com.practice.cafesystem.jwt.CustomerUsersDetailsService;
import com.practice.cafesystem.jwt.JwtMyFilter;
import com.practice.cafesystem.jwt.JwtUtils;
import com.practice.cafesystem.pojo.User;
import com.practice.cafesystem.service.UserService;
import com.practice.cafesystem.utils.CafeUtils;
import com.practice.cafesystem.utils.EmailUtils;
import com.practice.cafesystem.wrapper.UserWrapper;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    @Autowired
    JwtMyFilter jwtMyFilter;

    @Autowired
    EmailUtils emailUtils;

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
                if (user != null && Boolean.parseBoolean(user.getStatus())) {
                    String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
                    return new ResponseEntity<String>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Wait for Admin Approval", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("==>> ", e);
        }
        return CafeUtils.getResponseEntity("BAD Credentials", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (jwtMyFilter.isAdmin()) {
                return new ResponseEntity<>(userDAO.getAllUsers(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtMyFilter.isAdmin()) {
                Optional<User> optional = userDAO.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    userDAO.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));

                    sendEmailToAllAdmin(
                            requestMap.get("status"),
                            optional.get().getEmail(),
                            userDAO.getAllAdmin()
                    );

                    return CafeUtils.getResponseEntity("User updated sucessfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Used ID doesnt exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.BAD_REQUEST);
    }

    private void sendEmailToAllAdmin(String status, String userUpdated, List<String> allAdmins) {
        allAdmins.remove(jwtMyFilter.getCurrentUser()); // current logged account
        allAdmins.add(userUpdated); // The updated account

        String to = jwtMyFilter.getCurrentUser();
        String subject = "** Account DISABLED **";
        String text = "USER:\n " + userUpdated + "\nDISABLED by: \n" + to;

        if (status != null && status.equalsIgnoreCase("true")) {
            subject = "** Account Approved **";
            text = "USER:\n" + userUpdated + "\napproved by: \n" + to;
        }
        emailUtils.sendSimpleMessage(to, subject, text, allAdmins);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            String email = jwtMyFilter.getCurrentUser();
            User user = userDAO.findByEmailId(email);
            if (!Objects.isNull(user)) {
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userDAO.save(user);
                    return CafeUtils.getResponseEntity("Password Updated Sucessfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity("true", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDAO.findByEmailId(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
            }
            return CafeUtils.getResponseEntity("Check your Email For credentials", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity("true", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
