package com.drgproject.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "spare_parts_shipment")
public class SparePartsShipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region")
    private String region;

    @Column(name = "storage_name")
    private String storageName;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "spare_part_name")
    private String sparePartName;

    @Column(name = "spare_part_number")
    private Long sparePartNumber;

    @Column(name = "measure")
    private String measure;

    @Column(name = "transaction_type")
    private String transactionType; // "IN" for incoming, "OUT" for outgoing

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public SparePartsShipment() {
    }

    public SparePartsShipment(String region, String storageName, String employeeNumber,
                              String sparePartName, Long sparePartNumber, String measure,
                              String transactionType, double quantity, LocalDate dateCreate) {
        this.region = region;
        this.storageName = storageName;
        this.employeeNumber = employeeNumber;
        this.sparePartName = sparePartName;
        this.sparePartNumber = sparePartNumber;
        this.measure = measure;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getSparePartName() {
        return sparePartName;
    }

    public void setSparePartName(String sparePartName) {
        this.sparePartName = sparePartName;
    }

    public Long getSparePartNumber() {
        return sparePartNumber;
    }

    public void setSparePartNumber(Long sparePartNumber) {
        this.sparePartNumber = sparePartNumber;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}
