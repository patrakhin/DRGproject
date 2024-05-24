package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "loco_block")
public class LocoBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @ManyToOne
    @JoinColumn(name = "block_supply_id")
    private BlockSupply blockSupply;

    @ManyToOne
    @JoinColumn(name = "block_shipment_id")
    private BlockShipment blockShipment;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public LocoBlock(){}

    public LocoBlock(Storage storage, BlockSupply blockSupply, BlockShipment blockShipment,
                     String systemType, String blockName, String blockNumber, String status, LocalDate dateCreate) {
        this.storage = storage;
        this.blockSupply = blockSupply;
        this.blockShipment = blockShipment;
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.status = status;
        this.dateCreate = dateCreate;
    }

    // геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public BlockSupply getBlockSupply() {
        return blockSupply;
    }

    public void setBlockSupply(BlockSupply blockSupply) {
        this.blockSupply = blockSupply;
    }

    public BlockShipment getBlockShipment() {
        return blockShipment;
    }

    public void setBlockShipment(BlockShipment blockShipment) {
        this.blockShipment = blockShipment;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}

