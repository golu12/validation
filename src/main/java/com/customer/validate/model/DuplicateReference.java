package com.customer.validate.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class DuplicateReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) long id;

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
