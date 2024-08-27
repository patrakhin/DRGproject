package com.drgproject.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loco_block_two")
public class LocoBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "unique_id")
    private Long uniqueId;

    @Column(name = "region")
    private String region;

    @Column(name = "date_of_issue")
    private String dateOfIssue;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public LocoBlock(){}

    public LocoBlock(String systemType, String blockName,
                     String blockNumber, Long uniqueId, String region, String dateOfIssue) {
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.uniqueId = uniqueId;
        this.region = region;
        this.dateOfIssue = dateOfIssue;
    }

    // геттеры и сеттеры

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

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}
