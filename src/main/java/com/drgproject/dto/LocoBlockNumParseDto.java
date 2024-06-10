package com.drgproject.dto;

import java.util.List;

public class LocoBlockNumParseDto {
    private List<String> details;

    public LocoBlockNumParseDto(){}

    public LocoBlockNumParseDto(List<String> details) {
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
