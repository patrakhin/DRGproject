package com.drgproject.repair.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "block_on_loco")
public class BlockOnLoco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "block_number")
    private String blockNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loco_list_id")
    private LocoList locoList;

    public BlockOnLoco() {
    }

    public BlockOnLoco(String blockName, String blockNumber) {
        this.blockName = blockName;
        this.blockNumber = blockNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocoList getLocoList() {
        return locoList;
    }

    public void setLocoList(LocoList locoList) {
        this.locoList = locoList;
    }
}
