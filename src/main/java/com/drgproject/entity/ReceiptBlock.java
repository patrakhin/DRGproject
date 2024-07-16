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

    @Column(name = "region")
    private String region;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_number")
    private String blockNumber;

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

    public ReceiptBlock(String storageName,
                        String region,
                        String employeeNumber,
                        String systemType,
                        String blockName,
                        String blockNumber,
                        Long locoBlockUniqueId,
                        String transactionType,
                        int quantity) {
        this.storageName = storageName;
        this.region = region;
        this.employeeNumber = employeeNumber;
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
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
