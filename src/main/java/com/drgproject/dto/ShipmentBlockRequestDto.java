package com.drgproject.dto;

public class ShipmentBlockRequestDto {
    private String numberTable;
    private String systemType;
    private String nameBlock;
    private String blockNumber;

    public ShipmentBlockRequestDto(){}

    public ShipmentBlockRequestDto(String numberTable, String systemType, String nameBlock, String blockNumber) {
        this.numberTable = numberTable;
        this.systemType = systemType;
        this.nameBlock = nameBlock;
        this.blockNumber = blockNumber;
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
