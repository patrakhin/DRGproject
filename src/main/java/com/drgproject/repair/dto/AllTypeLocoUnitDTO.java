package com.drgproject.repair.dto;


public class AllTypeLocoUnitDTO {
    private Long id;
    private String typeLocoUnit;

    public AllTypeLocoUnitDTO(){}

    public AllTypeLocoUnitDTO(Long id, String typeLocoUnit) {
        this.id = id;
        this.typeLocoUnit = typeLocoUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeLocoUnit() {
        return typeLocoUnit;
    }

    public void setTypeLocoUnit(String typeLocoUnit) {
        this.typeLocoUnit = typeLocoUnit;
    }
}
