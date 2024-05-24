package com.drgproject.dto;

import com.drgproject.entity.LocoBlock;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.Storage}
 */
public class StorageDto implements Serializable {
    private Long id;
    private String name;
    private LocalDate dateCreate;
    @NotNull
    private List<LocoBlock> locoBlocks;

    public StorageDto(){}

    public StorageDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public List<LocoBlock> getLocoBlocks() {
        return locoBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageDto entity = (StorageDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.dateCreate, entity.dateCreate) &&
                Objects.equals(this.locoBlocks, entity.locoBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateCreate, locoBlocks);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "dateCreate = " + dateCreate + ", " +
                "locoBlocks = " + locoBlocks + ")";
    }
}