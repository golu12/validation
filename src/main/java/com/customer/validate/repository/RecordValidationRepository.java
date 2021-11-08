package com.customer.validate.repository;

import com.customer.validate.model.RecordsMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordValidationRepository extends JpaRepository<RecordsMapper, Integer> {
}
