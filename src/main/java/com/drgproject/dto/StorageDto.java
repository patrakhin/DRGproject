package com.drgproject.dto;

import com.drgproject.entity.LocoBlock;
import com.drgproject.entity.LocoBlockTransaction;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.Storage}
 */
public class StorageDto implements Serializable {
    @NotNull(message = "NotNull")
    private Long id;
    @NotNull(message = "NotNull")
    private String storageName;
    @NotNull(message = "NotNull")
    private LocalDate dateCreate;
    @NotNull(message = "NotNull")
    private List<LocoBlock> locoBlock;
    @NotNull
    private List<LocoBlockTransaction> transactions;

    public StorageDto(){}

    public StorageDto(String storageName, LocalDate dateCreate) {
        this.storageName = storageName;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public String getStorageName() {
        return storageName;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public List<LocoBlock> getLocoBlock() {
        return locoBlock;
    }

    public List<LocoBlockTransaction> getTransactions() {
        return transactions;
    }



    public void setId(@NotNull(message = "NotNull") Long id) {
        this.id = id;
    }

    public void setStorageName(@NotNull(message = "NotNull") String storageName) {
        this.storageName = storageName;
    }

    public void setDateCreate(@NotNull(message = "NotNull") LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setLocoBlock(@NotNull(message = "NotNull") List<LocoBlock> locoBlock) {
        this.locoBlock = locoBlock;
    }

    public void setTransactions(@NotNull List<LocoBlockTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageDto entity = (StorageDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.storageName, entity.storageName) &&
                Objects.equals(this.dateCreate, entity.dateCreate) &&
                Objects.equals(this.locoBlock, entity.locoBlock) &&
                Objects.equals(this.transactions, entity.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storageName, dateCreate, locoBlock, transactions);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "storageName = " + storageName + ", " +
                "dateCreate = " + dateCreate + ", " +
                "locoBlock = " + locoBlock + ", " +
                "transactions = " + transactions + ")";
    }
}