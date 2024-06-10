package com.drgproject.dto;

public class LocoBlockNumGetterDto {
    private Long uniqueId;

    public LocoBlockNumGetterDto(){}

    public LocoBlockNumGetterDto(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }
}
