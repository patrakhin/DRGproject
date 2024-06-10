package com.drgproject.dto;

import com.drgproject.entity.Locomotive;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.BlockInLoco}
 */
public class BlockInLocoDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String blockNumber;
    @NotNull
    private String blockName;
    @NotNull
    private String blockStatus;
    @NotNull
    private LocalDate dateCreate;
    @NotNull
    private Locomotive locomotive;

    public BlockInLocoDto(){}

    public BlockInLocoDto(String blockNumber, String blockName, String blockStatus,
                          LocalDate dateCreate) {
        this.blockNumber = blockNumber;
        this.blockName = blockName;
        this.blockStatus = blockStatus;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setBlockNumber(@NotNull String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setBlockName(@NotNull String blockName) {
        this.blockName = blockName;
    }

    public void setBlockStatus(@NotNull String blockStatus) {
        this.blockStatus = blockStatus;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setLocomotive(@NotNull Locomotive locomotive) {
        this.locomotive = locomotive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockInLocoDto entity = (BlockInLocoDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.blockNumber, entity.blockNumber) &&
                Objects.equals(this.blockName, entity.blockName) &&
                Objects.equals(this.blockStatus, entity.blockStatus) &&
                Objects.equals(this.dateCreate, entity.dateCreate) &&
                Objects.equals(this.locomotive, entity.locomotive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blockNumber, blockName, blockStatus, dateCreate, locomotive);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "blockNumber = " + blockNumber + ", " +
                "blockName = " + blockName + ", " +
                "blockStatus = " + blockStatus + ", " +
                "dateCreate = " + dateCreate + ", " +
                "locomotive = " + locomotive + ")";
    }
}