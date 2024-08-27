package com.drgproject.repair.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "block_name")
public class BlockName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_name")
    private String blockName;

    @ManyToOne
    @JoinColumn(name = "system_name_id", nullable = false)
    private SystemName systemName;

    public BlockName() {}

    public BlockName(String blockName, SystemName systemName) {
        this.blockName = blockName;
        this.systemName = systemName;
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

    public SystemName getSystemName() {
        return systemName;
    }

    public void setSystemName(SystemName systemName) {
        this.systemName = systemName;
    }
}
