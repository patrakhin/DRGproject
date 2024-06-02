package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "block_in")
    private String blockIn;

    @Column(name = "block_out")
    private String blockOut;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @ManyToOne
    @JoinColumn(name = "repair_history_id")
    private RepairHistory repairHistory;

    @PrePersist
    protected void onCreate() {
        this.dateCreate = LocalDate.now();
    }

    public TransactionLog(){}

    public TransactionLog(String systemType, String blockIn, String blockOut, LocalDate dateCreate) {
        this.systemType = systemType;
        this.blockIn = blockIn;
        this.blockOut = blockOut;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getBlockIn() {
        return blockIn;
    }

    public void setBlockIn(String blockIn) {
        this.blockIn = blockIn;
    }

    public String getBlockOut() {
        return blockOut;
    }

    public void setBlockOut(String blockOut) {
        this.blockOut = blockOut;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public RepairHistory getRepairHistory() {
        return repairHistory;
    }

    public void setRepairHistory(RepairHistory repairHistory) {
        this.repairHistory = repairHistory;
    }
}
