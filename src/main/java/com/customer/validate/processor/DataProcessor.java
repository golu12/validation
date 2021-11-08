package com.customer.validate.processor;

import org.springframework.batch.item.ItemProcessor;

public class DataProcessor implements ItemProcessor<Object, Object> {


    @Override
    public Object process(Object data) throws Exception {
        return data;
    }
}
