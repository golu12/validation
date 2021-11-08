package com.customer.validate.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class RecordsMapper {

    @Id
    @Column
    private int Reference;
    @Column
    private String AccountNumber;
    @Column
    private String Description;
    @Column
    private double StartBalance;
    @Column
    private double Mutation;
    @Column
    private double EndBalance ;
}
