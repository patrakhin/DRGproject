package com.drgproject.dto;

import com.drgproject.entity.BlockInLoco;
import com.drgproject.entity.RepairHistory;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.Locomotive}
 */
public class LocomotiveDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String locomotiveNumber;
    @NotNull
    private String section;
    @NotNull
    private String systemType;
    @NotNull
    private String homeDepot;
    @NotNull
    private LocalDate dateCreate;
    @NotNull
    private List<BlockInLoco> blocks;
    @NotNull
    private List<RepairHistory> repairHistories;

    public LocomotiveDto(){}

    public LocomotiveDto (String locomotiveNumber, String section, String systemType,
                         String homeDepot) {
        this.locomotiveNumber = locomotiveNumber;
        this.section = section;
        this.systemType = systemType;
        this.homeDepot = homeDepot;
    }

    public Long getId() {
        return id;
    }

    public String getLocomotiveNumber() {
        return locomotiveNumber;
    }

    public String getSection() {
        return section;
    }

    public String getSystemType() {
        return systemType;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public List<BlockInLoco> getBlocks() {
        return blocks;
    }

    public List<RepairHistory> getRepairHistories() {
        return repairHistories;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setLocomotiveNumber(@NotNull String locomotiveNumber) {
        this.locomotiveNumber = locomotiveNumber;
    }

    public void setSection(@NotNull String section) {
        this.section = section;
    }

    public void setSystemType(@NotNull String systemType) {
        this.systemType = systemType;
    }

    public void setHomeDepot(@NotNull String homeDepot) {
        this.homeDepot = homeDepot;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setBlocks(@NotNull List<BlockInLoco> blocks) {
        this.blocks = blocks;
    }

    public void setRepairHistories(@NotNull List<RepairHistory> repairHistories) {
        this.repairHistories = repairHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocomotiveDto entity = (LocomotiveDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.locomotiveNumber, entity.locomotiveNumber) &&
                Objects.equals(this.section, entity.section) &&
                Objects.equals(this.systemType, entity.systemType) &&
                Objects.equals(this.homeDepot, entity.homeDepot) &&
                Objects.equals(this.dateCreate, entity.dateCreate) &&
                Objects.equals(this.blocks, entity.blocks) &&
                Objects.equals(this.repairHistories, entity.repairHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locomotiveNumber, section, systemType, homeDepot, dateCreate, blocks, repairHistories);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "locomotiveNumber = " + locomotiveNumber + ", " +
                "section = " + section + ", " +
                "systemType = " + systemType + ", " +
                "homeDepot = " + homeDepot + ", " +
                "dateCreate = " + dateCreate + ", " +
                "blocks = " + blocks + ", " +
                "repairHistories = " + repairHistories + ")";
    }
}