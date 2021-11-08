package com.customer.validate.repository;

import com.customer.validate.model.DuplicateReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuplicateRefRepository extends JpaRepository<DuplicateReference, Long> {
}
