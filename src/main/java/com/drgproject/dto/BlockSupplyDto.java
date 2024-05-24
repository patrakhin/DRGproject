package com.drgproject.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.BlockSupply}
 */
public class BlockSupplyDto implements Serializable {
    private final Long id;
    @NotNull
    private final EmployeeDto employee;
    @NotNull
    private final StorageDto storage;
    private final LocalDate supplyDate;
    @NotNull
    private final List<LocoBlockDto> locoBlocks;

    public BlockSupplyDto(Long id, EmployeeDto employee, StorageDto storage, LocalDate supplyDate, List<LocoBlockDto> locoBlocks) {
        this.id = id;
        this.employee = employee;
        this.storage = storage;
        this.supplyDate = supplyDate;
        this.locoBlocks = locoBlocks;
    }

    public Long getId() {
        return id;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public StorageDto getStorage() {
        return storage;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public List<LocoBlockDto> getLocoBlocks() {
        return locoBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockSupplyDto entity = (BlockSupplyDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.employee, entity.employee) &&
                Objects.equals(this.storage, entity.storage) &&
                Objects.equals(this.supplyDate, entity.supplyDate) &&
                Objects.equals(this.locoBlocks, entity.locoBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee, storage, supplyDate, locoBlocks);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "employee = " + employee + ", " +
                "storage = " + storage + ", " +
                "supplyDate = " + supplyDate + ", " +
                "locoBlocks = " + locoBlocks + ")";
    }
}