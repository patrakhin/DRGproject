package com.drgproject.dto;

import com.drgproject.entity.ReceiptBlock;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ReceiptBlock}
 */
public class ReceiptBlockDto implements Serializable {

    private Long id;
    private String storageName;
    private String employeeNumber;
    private Long locoBlockUniqueId;
    private String transactionType;
    private int quantity;
    private LocalDate dateCreate;

    public ReceiptBlockDto(){}

    public ReceiptBlockDto(String storageName, String employeeNumber, Long locoBlockUniqueId,
                           String transactionType, int quantity) {
        this.storageName = storageName;
        this.employeeNumber = employeeNumber;
        this.locoBlockUniqueId = locoBlockUniqueId;
        this.transactionType = transactionType;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getLocoBlockUniqueId() {
        return locoBlockUniqueId;
    }

    public void setLocoBlockUniqueId(Long locoBlockUniqueId) {
        this.locoBlockUniqueId = locoBlockUniqueId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}