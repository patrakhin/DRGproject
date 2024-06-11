package com.drgproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_two")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "post")
    private String post;

    @Column
    private String unit;

    @Column(name = "region")
    private String region;

    @Column(name = "number_table")
    private String numberTable;

    public Employee(){}

    public Employee(String fio, String post, String unit, String region, String numberTable) {
        this.fio = fio;
        this.post = post;
        this.unit = unit;
        this.region = region;
        this.numberTable = numberTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNumberTable() {
        return numberTable;
    }

    public void setNumberTable(String numberTable) {
        this.numberTable = numberTable;
    }
}
