package com.customer.validate.service;

import com.customer.validate.model.RecordsMapper;

import java.util.Optional;

public interface RecordValidationService {

    public Optional<Object> validationOfInputFiles(RecordsMapper recordsMapper);
}
