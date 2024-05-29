package com.drgproject.dto;

import com.drgproject.entity.Employee;
import com.drgproject.entity.LocoBlock;
import com.drgproject.entity.Storage;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.LocoBlockTransaction}
 */
public class LocoBlockTransactionDto implements Serializable {
    @NotNull(message = "NotNull")
    private Long id;
    @NotNull(message = "NotNull")
    private Storage storage;
    @NotNull(message = "NotNull")
    private LocoBlock locoBlock;
    @NotNull(message = "NotNull")
    private Employee employee;
    @NotNull(message = "NotNull")
    private String transactionType;
    private int quantity;
    @NotNull
    private LocalDate dateCreate;

    public LocoBlockTransactionDto(){}

    public LocoBlockTransactionDto(Storage storage, LocoBlock locoBlock, Employee employee,
                                   String transactionType, int quantity) {
        this.storage = storage;
        this.locoBlock = locoBlock;
        this.employee = employee;
        this.transactionType = transactionType;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Storage getStorage() {
        return storage;
    }

    public LocoBlock getLocoBlock() {
        return locoBlock;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setId(@NotNull(message = "NotNull") Long id) {
        this.id = id;
    }

    public void setStorage(@NotNull(message = "NotNull") Storage storage) {
        this.storage = storage;
    }

    public void setLocoBlock(@NotNull(message = "NotNull") LocoBlock locoBlock) {
        this.locoBlock = locoBlock;
    }

    public void setEmployee(@NotNull(message = "NotNull") Employee employee) {
        this.employee = employee;
    }

    public void setTransactionType(@NotNull(message = "NotNull") String transactionType) {
        this.transactionType = transactionType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocoBlockTransactionDto entity = (LocoBlockTransactionDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.storage, entity.storage) &&
                Objects.equals(this.locoBlock, entity.locoBlock) &&
                Objects.equals(this.employee, entity.employee) &&
                Objects.equals(this.transactionType, entity.transactionType) &&
                Objects.equals(this.quantity, entity.quantity) &&
                Objects.equals(this.dateCreate, entity.dateCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storage, locoBlock, employee, transactionType, quantity, dateCreate);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "storage = " + storage + ", " +
                "locoBlock = " + locoBlock + ", " +
                "employee = " + employee + ", " +
                "transactionType = " + transactionType + ", " +
                "quantity = " + quantity + ", " +
                "dateCreate = " + dateCreate + ")";
    }
}