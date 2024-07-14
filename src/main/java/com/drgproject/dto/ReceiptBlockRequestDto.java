package com.drgproject.dto;

public class ReceiptBlockRequestDto {
    private String storageName;
    private String region;
    private String numberTable;
    private String systemType;
    private String nameBlock;
    private String blockNumber;

    public ReceiptBlockRequestDto(){}

    public ReceiptBlockRequestDto(String storageName, String region, String numberTable, String systemType,
                                  String nameBlock, String blockNumber) {
        this.storageName = storageName;
        this.region = region;
        this.numberTable = numberTable;
        this.systemType = systemType;
        this.nameBlock = nameBlock;
        this.blockNumber = blockNumber;
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

    public String getNumberTable() {
        return numberTable;
    }

    public void setNumberTable(String numberTable) {
        this.numberTable = numberTable;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getNameBlock() {
        return nameBlock;
    }

    public void setNameBlock(String nameBlock) {
        this.nameBlock = nameBlock;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }
}
