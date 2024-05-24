package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "block_shipment")
public class BlockShipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column(name = "shipment_date")
    private LocalDate shipmentDate;

    @OneToMany(mappedBy = "blockShipment")
    private List<LocoBlock> locoBlocks;

    @PrePersist
    protected void onCreate() {
        shipmentDate = LocalDate.now();
    }

    public BlockShipment(){}

    public BlockShipment(Employee employee, Storage storage, LocalDate shipmentDate, List<LocoBlock> locoBlocks) {
        this.employee = employee;
        this.storage = storage;
        this.shipmentDate = shipmentDate;
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

    public LocalDate getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDate shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public List<LocoBlock> getLocoBlocks() {
        return locoBlocks;
    }

    public void setLocoBlocks(List<LocoBlock> locoBlocks) {
        this.locoBlocks = locoBlocks;
    }
}

