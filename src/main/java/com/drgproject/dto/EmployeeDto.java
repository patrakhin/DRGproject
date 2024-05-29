package com.drgproject.dto;

import com.drgproject.entity.LocoBlockTransaction;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
    @NotNull
    private List<LocoBlockTransaction> transactions;

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

    public List<LocoBlockTransaction> getTransactions() {
        return transactions;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setFio(@NotNull String fio) {
        this.fio = fio;
    }

    public void setPost(@NotNull String post) {
        this.post = post;
    }

    public void setUnit(@NotNull String unit) {
        this.unit = unit;
    }

    public void setRegion(@NotNull String region) {
        this.region = region;
    }

    public void setNumberTable(@NotNull String numberTable) {
        this.numberTable = numberTable;
    }

    public void setTransactions(@NotNull List<LocoBlockTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDto entity = (EmployeeDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.fio, entity.fio) &&
                Objects.equals(this.post, entity.post) &&
                Objects.equals(this.unit, entity.unit) &&
                Objects.equals(this.region, entity.region) &&
                Objects.equals(this.numberTable, entity.numberTable) &&
                Objects.equals(this.transactions, entity.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fio, post, unit, region, numberTable, transactions);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "fio = " + fio + ", " +
                "post = " + post + ", " +
                "unit = " + unit + ", " +
                "region = " + region + ", " +
                "numberTable = " + numberTable + ", " +
                "transactions = " + transactions + ")";
    }
}