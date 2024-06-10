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
    private String transactionType;
    private int quantity;
    @NotNull
    private LocalDate dateCreate;

    public LocoBlockTransactionDto(){}

    public LocoBlockTransactionDto(String transactionType, int quantity) {
        this.transactionType = transactionType;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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

    public void setTransactionType(@NotNull(message = "NotNull") String transactionType) {
        this.transactionType = transactionType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}