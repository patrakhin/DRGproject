package com.drgproject.repair.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "block_removal")
public class BlockRemoval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_loco")
    private String typeLoco;

    @Column(name = "loco_number")
    private String locoNumber;

    @Column(name = "region")
    private String region;

    @Column(name = "home_depot")
    private String homeDepot;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "dlock_name")
    private String blockName;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "date_of_issue")
    private String dateOfIssue;

    @Column(name = "number_table")
    private String numberTable;

    @Column(name = "position")
    private String position;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public BlockRemoval(){}

    public BlockRemoval(String typeLoco, String locoNumber, String region, String homeDepot,
                        String systemType, String blockName, String blockNumber, String dateOfIssue, String numberTable, String position) {
        this.typeLoco = typeLoco;
        this.locoNumber = locoNumber;
        this.region = region;
        this.homeDepot = homeDepot;
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.dateOfIssue = dateOfIssue;
        this.numberTable = numberTable;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeLoco() {
        return typeLoco;
    }

    public void setTypeLoco(String typeLoco) {
        this.typeLoco = typeLoco;
    }

    public String getLocoNumber() {
        return locoNumber;
    }

    public void setLocoNumber(String locoNumber) {
        this.locoNumber = locoNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public void setHomeDepot(String homeDepot) {
        this.homeDepot = homeDepot;
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

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getNumberTable() {
        return numberTable;
    }

    public void setNumberTable(String numberTable) {
        this.numberTable = numberTable;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }
}
