package com.customer.validate.service;

import com.customer.validate.model.DuplicateReference;
import com.customer.validate.model.RecordsMapper;
import com.customer.validate.model.WrongSumRecords;
import com.customer.validate.reader.XmlReader;
import com.customer.validate.repository.DuplicateRefRepository;
import com.customer.validate.repository.RecordValidationRepository;
import com.customer.validate.repository.WrongSumsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("RecordValidationService")
public class RecordValidationServiceImpl implements RecordValidationService {
    private static final Logger LOG = LoggerFactory.getLogger(RecordValidationServiceImpl.class);

    @Autowired
    private RecordValidationRepository recordValidationRepository;

    @Autowired
    private DuplicateRefRepository duplicateRefRepository;

    @Autowired
    private WrongSumsRepository wrongSumsRepository;

    @Override
    public Optional<Object> validationOfInputFiles(RecordsMapper recordsMapper) {
        WrongSumRecords wrongSumRecords= null;
        DuplicateReference duplicateReference = null;
        try{
            Optional<RecordsMapper> referenceRecord = Optional.ofNullable(recordValidationRepository.findOne(recordsMapper.getReference()));
            if(!referenceRecord.isPresent()){
                double sums = recordsMapper.getStartBalance() + recordsMapper.getMutation();
                if(Double.compare(Double.parseDouble((String.format("%.2f", sums))), recordsMapper.getEndBalance()) == 0){
                recordValidationRepository.save(recordsMapper);
                } else {
                wrongSumRecords = new WrongSumRecords();
                wrongSumRecords.setReference(recordsMapper.getReference());
                wrongSumRecords.setDescription(recordsMapper.getDescription());
                wrongSumRecords.setAccountNumber(recordsMapper.getAccountNumber());
                wrongSumRecords.setStartBalance(recordsMapper.getStartBalance());
                wrongSumRecords.setMutation(recordsMapper.getMutation());
                wrongSumRecords.setEndBalance(recordsMapper.getEndBalance());
                wrongSumsRepository.save(wrongSumRecords);
                }
            } else {
                duplicateReference = new DuplicateReference();
                duplicateReference.setReference(recordsMapper.getReference());
                duplicateReference.setDescription(recordsMapper.getDescription());
                duplicateReference.setAccountNumber(recordsMapper.getAccountNumber());
                duplicateReference.setStartBalance(recordsMapper.getStartBalance());
                duplicateReference.setMutation(recordsMapper.getMutation());
                duplicateReference.setEndBalance(recordsMapper.getEndBalance());
                duplicateRefRepository.save(duplicateReference);
            }
        }catch(Exception e){
            LOG.error("Error while validation of files" , e.getMessage());
        }
        return Optional.empty();
    }
}
