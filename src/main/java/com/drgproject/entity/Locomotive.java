package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "locomotive")
public class Locomotive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locomotive_number")
    private String locomotiveNumber;

    @Column(name = "section")
    private String section;

    @Column(name = "system_type")
    private String systemType;

    @Column(name = "home_depot")
    private String homeDepot;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @OneToMany(mappedBy = "locomotive", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockInLoco> blocks;

    @OneToMany(mappedBy = "locomotive", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepairHistory> repairHistories;

    @PrePersist
    protected void onCreate() {
        this.dateCreate = LocalDate.now();
    }

    public Locomotive(){}

    public Locomotive(String locomotiveNumber, String section, String systemType, String homeDepot, LocalDate dateCreate) {
        this.locomotiveNumber = locomotiveNumber;
        this.section = section;
        this.systemType = systemType;
        this.homeDepot = homeDepot;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocomotiveNumber() {
        return locomotiveNumber;
    }

    public void setLocomotiveNumber(String locomotiveNumber) {
        this.locomotiveNumber = locomotiveNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public void setHomeDepot(String homeDepot) {
        this.homeDepot = homeDepot;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public List<BlockInLoco> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockInLoco> blocks) {
        this.blocks = blocks;
    }

    public List<RepairHistory> getRepairHistories() {
        return repairHistories;
    }

    public void setRepairHistories(List<RepairHistory> repairHistories) {
        this.repairHistories = repairHistories;
    }
}