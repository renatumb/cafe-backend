package com.practice.cafesystem.dao;

import com.practice.cafesystem.pojo.Bill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BillDAO extends JpaRepository<Bill, Integer> {
    List<Bill> getAllBills();

    List<Bill> getBillsByUserName(@Param("username") String currentUser);
}
