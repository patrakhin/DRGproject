package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "receipt_block")
public class ReceiptBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_name")
    private String storageName;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "locoblock_unique_id")
    private Long locoBlockUniqueId;

    @Column(name = "transaction_type")
    private String transactionType; // "IN" for incoming, "OUT" for outgoing

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public ReceiptBlock(){}

    public ReceiptBlock(String storageName, String employeeNumber, Long locoBlockUniqueId,
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
