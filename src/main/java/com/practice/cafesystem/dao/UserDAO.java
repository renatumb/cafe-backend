package com.practice.cafesystem.dao;

import com.practice.cafesystem.pojo.User;
import com.practice.cafesystem.wrapper.UserWrapper;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends JpaRepository<User, Integer> {

    User findByEmailId(@Param("email") String Email);
    List<UserWrapper> getAllUsers();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param( "id")int id);

    List<String> getAllAdmin();
}
