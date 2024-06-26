package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "loco_block")
public class LocoBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "locoblock_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<LocoBlockTransaction> locoBlocks;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public LocoBlock(){}

    public LocoBlock(String systemType, String blockName,
                     String blockNumber, LocalDate dateCreate) {
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.dateCreate = dateCreate;
    }

    // геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LocoBlockTransaction> getLocoBlocks() {
        return locoBlocks;
    }

    public void setLocoBlocks(List<LocoBlockTransaction> locoBlocks) {
        this.locoBlocks = locoBlocks;
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

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}

