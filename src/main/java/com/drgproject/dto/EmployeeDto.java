package com.drgproject.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for {@link com.drgproject.entity.Employee}
 */
public class EmployeeDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String fio;
    @NotNull
    private String post;
    @NotNull
    private String unit;
    @NotNull
    private String region;
    @NotNull
    private String numberTable;

    public EmployeeDto(){}

    public EmployeeDto(String fio, String post, String unit, String region,
                       String numberTable) {
        this.fio = fio;
        this.post = post;
        this.unit = unit;
        this.region = region;
        this.numberTable = numberTable;
    }

    public Long getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getPost() {
        return post;
    }

    public String getUnit() {
        return unit;
    }

    public String getRegion() {
        return region;
    }

    public String getNumberTable() {
        return numberTable;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setNumberTable(@NotNull String numberTable) {
        this.numberTable = numberTable;
    }

}