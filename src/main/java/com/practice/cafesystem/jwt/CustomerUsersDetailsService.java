package com.practice.cafesystem.jwt;

import com.practice.cafesystem.dao.UserDAO;
import com.practice.cafesystem.pojo.User;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // AAA
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    public UserDAO userDAO;
    private User user;

    public User getUser() {
        return user;
    }

    @Override /// AAA
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = userDAO.findByEmailId(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
