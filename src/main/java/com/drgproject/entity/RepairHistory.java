package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "repair_history")
public class RepairHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repair_type")
    private String repairType;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @OneToMany(mappedBy = "repairHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionLog> transactionLogs;

    @ManyToOne
    @JoinColumn(name = "locomotive_id")
    private Locomotive locomotive;

    @PrePersist
    protected void onCreate() {
        this.dateCreate = LocalDate.now();
    }

    public RepairHistory(){}

    public RepairHistory(String repairType, String employeeNumber, LocalDate dateCreate) {
        this.repairType = repairType;
        this.employeeNumber = employeeNumber;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public void setLocomotive(Locomotive locomotive) {
        this.locomotive = locomotive;
    }

    public List<TransactionLog> getTransactionLogs() {
        return transactionLogs;
    }

    public void setTransactionLogs(List<TransactionLog> transactionLogs) {
        this.transactionLogs = transactionLogs;
    }
}
