package com.drgproject.dto;

import com.drgproject.entity.BlockShipment;
import com.drgproject.entity.BlockSupply;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.LocoBlock}
 */
public class LocoBlockDto implements Serializable {
    private Long id;
    @NotNull
    private StorageDto storage;
    @NotNull
    private BlockSupply blockSupply;
    @NotNull
    private BlockShipment blockShipment;
    private String systemType;
    private String blockName;
    private String blockNumber;
    private String status;
    private LocalDate dateCreate;

    public LocoBlockDto(){}

    public LocoBlockDto(StorageDto storage, BlockSupply blockSupply, BlockShipment blockShipment,
                        String systemType, String blockName, String blockNumber, String status, LocalDate dateCreate) {
        this.storage = storage;
        this.blockSupply = blockSupply;
        this.blockShipment = blockShipment;
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.status = status;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull StorageDto getStorage() {
        return storage;
    }

    public void setStorage(@NotNull StorageDto storage) {
        this.storage = storage;
    }

    public @NotNull BlockSupply getBlockSupply() {
        return blockSupply;
    }

    public void setBlockSupply(@NotNull BlockSupply blockSupply) {
        this.blockSupply = blockSupply;
    }

    public @NotNull BlockShipment getBlockShipment() {
        return blockShipment;
    }

    public void setBlockShipment(@NotNull BlockShipment blockShipment) {
        this.blockShipment = blockShipment;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocoBlockDto entity = (LocoBlockDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.storage, entity.storage) &&
                Objects.equals(this.blockSupply, entity.blockSupply) &&
                Objects.equals(this.blockShipment, entity.blockShipment) &&
                Objects.equals(this.systemType, entity.systemType) &&
                Objects.equals(this.blockName, entity.blockName) &&
                Objects.equals(this.blockNumber, entity.blockNumber) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.dateCreate, entity.dateCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storage, blockSupply, blockShipment, systemType, blockName, blockNumber, status, dateCreate);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "storage = " + storage + ", " +
                "blockSupply = " + blockSupply + ", " +
                "blockShipment = " + blockShipment + ", " +
                "systemType = " + systemType + ", " +
                "blockName = " + blockName + ", " +
                "blockNumber = " + blockNumber + ", " +
                "status = " + status + ", " +
                "dateCreate = " + dateCreate + ")";
    }
}