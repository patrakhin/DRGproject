package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "block_supply")
public class BlockSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column(name = "supply_date")
    private LocalDate supplyDate;

    @OneToMany(mappedBy = "blockSupply")
    private List<LocoBlock> locoBlocks;

    @PrePersist
    protected void onCreate() {
        supplyDate = LocalDate.now();
    }

    public BlockSupply(){}

    public BlockSupply(Employee employee, Storage storage, LocalDate supplyDate, List<LocoBlock> locoBlocks) {
        this.employee = employee;
        this.storage = storage;
        this.supplyDate = supplyDate;
        this.locoBlocks = locoBlocks;
    }

    // геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(LocalDate supplyDate) {
        this.supplyDate = supplyDate;
    }

    public List<LocoBlock> getLocoBlocks() {
        return locoBlocks;
    }

    public void setLocoBlocks(List<LocoBlock> locoBlocks) {
        this.locoBlocks = locoBlocks;
    }
}

