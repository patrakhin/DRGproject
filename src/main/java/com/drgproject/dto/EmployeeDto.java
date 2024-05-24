package com.drgproject.dto;

import com.drgproject.entity.BlockShipment;
import com.drgproject.entity.BlockSupply;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.Employee}
 */
public class EmployeeDto implements Serializable {
    private final Long id;
    private final String fio;
    private final String post;
    private final String unit;
    private final String region;
    private final String numberTable;
    private final LocalDate operationDate;
    @NotNull
    private final List<BlockSupply> blockSupplies;
    @NotNull
    @Size
    private final List<BlockShipment> blockShipments;

    public EmployeeDto(Long id, String fio, String post, String unit, String region, String numberTable, LocalDate operationDate, List<BlockSupply> blockSupplies, List<BlockShipment> blockShipments) {
        this.id = id;
        this.fio = fio;
        this.post = post;
        this.unit = unit;
        this.region = region;
        this.numberTable = numberTable;
        this.operationDate = operationDate;
        this.blockSupplies = blockSupplies;
        this.blockShipments = blockShipments;
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

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public List<BlockSupply> getBlockSupplies() {
        return blockSupplies;
    }

    public List<BlockShipment> getBlockShipments() {
        return blockShipments;
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
                Objects.equals(this.operationDate, entity.operationDate) &&
                Objects.equals(this.blockSupplies, entity.blockSupplies) &&
                Objects.equals(this.blockShipments, entity.blockShipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fio, post, unit, region, numberTable, operationDate, blockSupplies, blockShipments);
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
                "operationDate = " + operationDate + ", " +
                "blockSupplies = " + blockSupplies + ", " +
                "blockShipments = " + blockShipments + ")";
    }
}