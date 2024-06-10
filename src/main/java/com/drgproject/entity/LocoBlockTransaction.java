package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loco_block_transaction")
public class LocoBlockTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*    @ManyToOne
    @JoinColumn(name = "storage_id", referencedColumnName = "id") //ok!
    private Storage storage;

    @ManyToOne
    @JoinColumn(name = "locoblock_id", referencedColumnName = "id") //ok!
    private LocoBlock locoBlocks;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id") //ok!
    private Employee employee;*/

    @Column(name = "storage_id")
    private Long storageId;

    @Column(name = "locoblock_id")
    private Long locoBlockId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "transaction_type")
    private String transactionType; // "IN" for incoming, "OUT" for outgoing

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public LocoBlockTransaction(){}

    public LocoBlockTransaction(String transactionType, int quantity, LocalDate dateCreate) {
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

/*    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public LocoBlock getLocoBlock() {
        return locoBlocks;
    }

    public void setLocoBlock(LocoBlock locoBlock) {
        this.locoBlocks = locoBlock;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }*/

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getStorageId() {
        return storageId;
    }

    public Long getLocoBlockId() {
        return locoBlockId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public void setLocoBlockId(Long locoBlockId) {
        this.locoBlockId = locoBlockId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
