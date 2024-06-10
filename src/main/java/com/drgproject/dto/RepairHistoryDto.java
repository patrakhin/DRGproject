package com.drgproject.dto;

import com.drgproject.entity.Locomotive;
import com.drgproject.entity.TransactionLog;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.drgproject.entity.RepairHistory}
 */
public class RepairHistoryDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String repairType;
    @NotNull
    private String employeeNumber;
    @NotNull
    private LocalDate dateCreate;
    @NotNull
    private List<TransactionLog> transactionLogs;
    @NotNull
    private Locomotive locomotive;

    public RepairHistoryDto(){}

    public RepairHistoryDto(String repairType, String employeeNumber,
                            LocalDate dateCreate) {
        this.repairType = repairType;
        this.employeeNumber = employeeNumber;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public String getRepairType() {
        return repairType;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public List<TransactionLog> getTransactionLogs() {
        return transactionLogs;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setRepairType(@NotNull String repairType) {
        this.repairType = repairType;
    }

    public void setEmployeeNumber(@NotNull String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setDateCreate(@NotNull LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setTransactionLogs(@NotNull List<TransactionLog> transactionLogs) {
        this.transactionLogs = transactionLogs;
    }

    public void setLocomotive(@NotNull Locomotive locomotive) {
        this.locomotive = locomotive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepairHistoryDto entity = (RepairHistoryDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.repairType, entity.repairType) &&
                Objects.equals(this.employeeNumber, entity.employeeNumber) &&
                Objects.equals(this.dateCreate, entity.dateCreate) &&
                Objects.equals(this.transactionLogs, entity.transactionLogs) &&
                Objects.equals(this.locomotive, entity.locomotive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, repairType, employeeNumber, dateCreate, transactionLogs, locomotive);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "repairType = " + repairType + ", " +
                "employeeNumber = " + employeeNumber + ", " +
                "dateCreate = " + dateCreate + ", " +
                "transactionLogs = " + transactionLogs + ", " +
                "locomotive = " + locomotive + ")";
    }
}