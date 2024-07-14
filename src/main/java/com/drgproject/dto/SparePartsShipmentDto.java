package com.drgproject.dto;

import com.drgproject.entity.SparePartsShipment;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link SparePartsShipment}
 */
public class SparePartsShipmentDto implements Serializable{
    private Long id;
    private String region;
    private String storageName;
    private String employeeNumber;
    private String sparePartName;
    private String measure;
    private Long sparePartNumber;
    private String transactionType; // "IN" for incoming, "OUT" for outgoing
    private double quantity;

    private LocalDate dateCreate;

    public SparePartsShipmentDto(){}

    public SparePartsShipmentDto(String region, String storageName, String employeeNumber,
                                 String sparePartName, String measure, Long sparePartNumber,
                                 String transactionType, double quantity) {
        this.region = region;
        this.storageName = storageName;
        this.employeeNumber = employeeNumber;
        this.sparePartName = sparePartName;
        this.measure = measure;
        this.sparePartNumber = sparePartNumber;
        this.transactionType = transactionType;
        this.quantity = quantity;
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

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Long getSparePartNumber() {
        return sparePartNumber;
    }

    public void setSparePartNumber(Long sparePartNumber) {
        this.sparePartNumber = sparePartNumber;
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
