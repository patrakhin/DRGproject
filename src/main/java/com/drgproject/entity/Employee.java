package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "post")
    private String post;

    @Column(name = "unit")
    private String unit;

    @Column(name = "region")
    private String region;

    @Column(name = "number_table")
    private String numberTable;

    @Column(name = "operation_date")
    private LocalDate operationDate;

    @OneToMany(mappedBy = "employee")
    private List<BlockSupply> blockSupplies;

    @OneToMany(mappedBy = "employee")
    private List<BlockShipment> blockShipments;

    @PrePersist
    protected void onCreate() {
        operationDate = LocalDate.now();
    }

    public Employee(){}

    public Employee(String fio, String post, String unit, String region, String numberTable,
                    LocalDate operationDate, List<BlockSupply> blockSupplies, List<BlockShipment> blockShipments) {
        this.fio = fio;
        this.post = post;
        this.unit = unit;
        this.region = region;
        this.numberTable = numberTable;
        this.operationDate = operationDate;
        this.blockSupplies = blockSupplies;
        this.blockShipments = blockShipments;
    }

    // геттеры и сеттеры

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

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDate operationDate) {
        this.operationDate = operationDate;
    }

    public List<BlockSupply> getBlockSupplies() {
        return blockSupplies;
    }

    public void setBlockSupplies(List<BlockSupply> blockSupplies) {
        this.blockSupplies = blockSupplies;
    }

    public List<BlockShipment> getBlockShipments() {
        return blockShipments;
    }

    public void setBlockShipments(List<BlockShipment> blockShipments) {
        this.blockShipments = blockShipments;
    }
}
