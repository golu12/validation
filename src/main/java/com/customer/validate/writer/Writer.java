package com.customer.validate.writer;

import com.customer.validate.model.Records;
import com.customer.validate.model.RecordsMapper;
import com.customer.validate.reader.XmlReader;
import com.customer.validate.service.RecordValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Writer implements ItemWriter<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(Writer.class);
    @Autowired
    private RecordValidationService recordValidationService;
    @Override
    public void write(List<? extends Object> message) throws Exception {
        RecordsMapper recordsMapper = null;
        if(message != null){
            for(Object obj : message){
                recordsMapper = ObjectMapping((Records) obj);
                recordValidationService.validationOfInputFiles(recordsMapper);
            }
        }

    }

    private RecordsMapper ObjectMapping(Records obj) {
        RecordsMapper recordsMapper = new RecordsMapper();
        recordsMapper.setReference(Integer.parseInt(obj.getReference()));
        recordsMapper.setAccountNumber(obj.getAccountNumber());
        recordsMapper.setDescription(obj.getDescription());
        recordsMapper.setEndBalance(Double.parseDouble(obj.getEndBalance()));
        recordsMapper.setMutation(Double.parseDouble(obj.getMutation()));
        recordsMapper.setStartBalance(Double.parseDouble(obj.getStartBalance()));
        return recordsMapper;
    }
}
