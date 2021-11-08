package com.customer.validate.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class Records {


    private String Reference;
    private String AccountNumber;
    private String Description;
    private String StartBalance;
    private String Mutation;
    private String EndBalance;
}
