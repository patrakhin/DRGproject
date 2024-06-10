package com.drgproject.dto;

import com.drgproject.entity.RepairHistory;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.TransactionLog}
 */
public class TransactionLogDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String systemType;
    @NotNull
    private String blockIn;
    @NotNull
    private String blockOut;
    @NotNull
    private LocalDate dateCreate;
    @NotNull
    private RepairHistory repairHistory;

    public TransactionLogDto(){}

    public TransactionLogDto(String systemType, String blockIn, String blockOut) {
        this.systemType = systemType;
        this.blockIn = blockIn;
        this.blockOut = blockOut;
    }

    public Long getId() {
        return id;
    }

    public String getSystemType() {
        return systemType;
    }

    public String getBlockIn() {
        return blockIn;
    }

    public String getBlockOut() {
        return blockOut;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public RepairHistory getRepairHistory() {
        return repairHistory;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setSystemType(@NotNull String systemType) {
        this.systemType = systemType;
    }

    public void setBlockIn(@NotNull String blockIn) {
        this.blockIn = blockIn;
    }

    public void setBlockOut(@NotNull String blockOut) {
        this.blockOut = blockOut;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setRepairHistory(@NotNull RepairHistory repairHistory) {
        this.repairHistory = repairHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionLogDto entity = (TransactionLogDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.systemType, entity.systemType) &&
                Objects.equals(this.blockIn, entity.blockIn) &&
                Objects.equals(this.blockOut, entity.blockOut) &&
                Objects.equals(this.dateCreate, entity.dateCreate) &&
                Objects.equals(this.repairHistory, entity.repairHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, systemType, blockIn, blockOut, dateCreate, repairHistory);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "systemType = " + systemType + ", " +
                "blockIn = " + blockIn + ", " +
                "blockOut = " + blockOut + ", " +
                "dateCreate = " + dateCreate + ", " +
                "repairHistory = " + repairHistory + ")";
    }
}