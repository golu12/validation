package com.customer.validate.repository;

import com.customer.validate.model.WrongSumRecords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WrongSumsRepository extends JpaRepository<WrongSumRecords, Long> {
}
