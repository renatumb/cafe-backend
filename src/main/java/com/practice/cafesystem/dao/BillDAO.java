package com.practice.cafesystem.dao;

import com.practice.cafesystem.pojo.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDAO extends JpaRepository<Bill, Integer> {
}
