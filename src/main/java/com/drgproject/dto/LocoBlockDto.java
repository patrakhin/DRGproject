package com.drgproject.dto;

import com.drgproject.entity.Storage;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.LocoBlock}
 */
public class LocoBlockDto implements Serializable {
    @NotNull(message = "NotNull")
    private Long id;
    @NotNull
    private String systemType;
    @NotNull
    private String blockName;
    @NotNull
    private String blockNumber;
    @NotNull
    private LocalDate dateCreate;

    public LocoBlockDto(){}

    public LocoBlockDto(String systemType,
                        String blockName, String blockNumber) {
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
    }

    public Long getId() {
        return id;
    }

    public String getSystemType() {
        return systemType;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setId(@NotNull(message = "NotNull") Long id) {
        this.id = id;
    }

    public void setSystemType(@NotNull String systemType) {
        this.systemType = systemType;
    }

    public void setBlockName(@NotNull String blockName) {
        this.blockName = blockName;
    }

    public void setBlockNumber(@NotNull String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocoBlockDto entity = (LocoBlockDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.systemType, entity.systemType) &&
                Objects.equals(this.blockName, entity.blockName) &&
                Objects.equals(this.blockNumber, entity.blockNumber) &&
                Objects.equals(this.dateCreate, entity.dateCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, systemType, blockName, blockNumber, dateCreate);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "systemType = " + systemType + ", " +
                "blockName = " + blockName + ", " +
                "blockNumber = " + blockNumber + ", " +
                "dateCreate = " + dateCreate + ")";
    }
}