package com.drgproject.dto;

import com.drgproject.entity.ShipmentBlock;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ShipmentBlock}
 */
public class ShipmentBlockDto implements Serializable {
    private Long id;
    private String storageName;
    private String employeeNumber;
    private String systemType;
    private String blockName;
    private String blockNumber;
    private Long locoBlockUniqueId;
    private String transactionType; // "IN" for incoming, "OUT" for outgoing
    private int quantity;
    private String region;
    private LocalDate dateCreate;

    public ShipmentBlockDto(){}

    public ShipmentBlockDto(String storageName, String employeeNumber, String systemType,
                            String blockName, String blockNumber, Long locoBlockUniqueId,
                            String transactionType, int quantity, String region) {
        this.storageName = storageName;
        this.employeeNumber = employeeNumber;
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.locoBlockUniqueId = locoBlockUniqueId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.region = region;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}
