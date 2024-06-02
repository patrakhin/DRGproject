package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "block_in_loco")
public class BlockInLoco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_number")
    private String blockNumber;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_status")
    private String blockStatus;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @ManyToOne
    @JoinColumn(name = "locomotive_id")
    private Locomotive locomotive;

    @PrePersist
    protected void onCreate(){
        this.dateCreate = LocalDate.now();
    }

    public BlockInLoco(){}

    public BlockInLoco(String blockNumber, String blockName, String blockStatus, LocalDate dateCreate) {
        this.blockNumber = blockNumber;
        this.blockName = blockName;
        this.blockStatus = blockStatus;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreat) {
        this.dateCreate = dateCreat;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    public void setLocomotive(Locomotive locomotive) {
        this.locomotive = locomotive;
    }
}
