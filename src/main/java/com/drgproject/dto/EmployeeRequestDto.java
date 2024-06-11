package com.drgproject.dto;

public class EmployeeRequestDto {
    private String numberTable;

    public EmployeeRequestDto(){}

    public EmployeeRequestDto(String numberTable) {
        this.numberTable = numberTable;
    }

    public String getNumberTable() {
        return numberTable;
    }

    public void setNumberTable(String numberTable) {
        this.numberTable = numberTable;
    }
}
