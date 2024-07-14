package com.drgproject.repair.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loco_list")
public class LocoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "type_loco")
    private String typeLoco;

    @Column(name = "type_system")
    private String typeSystem;

    @Column(name = "loco_number")
    private String locoNumber;

    @Column(name = "home_region")
    private String homeRegion;

    @Column(name = "home_depot")
    private String homeDepot;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "locoList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockOnLoco> blockOnLocos = new ArrayList<>();

    public LocoList() {
    }

    public LocoList(String contractNumber, String typeLoco, String typeSystem, String locoNumber,
                    String homeRegion, String homeDepot, String comment) {
        this.contractNumber = contractNumber;
        this.typeLoco = typeLoco;
        this.typeSystem = typeSystem;
        this.locoNumber = locoNumber;
        this.homeRegion = homeRegion;
        this.homeDepot = homeDepot;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getTypeLoco() {
        return typeLoco;
    }

    public void setTypeLoco(String typeLoco) {
        this.typeLoco = typeLoco;
    }

    public String getTypeSystem() {
        return typeSystem;
    }

    public void setTypeSystem(String typeSystem) {
        this.typeSystem = typeSystem;
    }

    public String getLocoNumber() {
        return locoNumber;
    }

    public void setLocoNumber(String locoNumber) {
        this.locoNumber = locoNumber;
    }

    public String getHomeRegion() {
        return homeRegion;
    }

    public void setHomeRegion(String homeRegion) {
        this.homeRegion = homeRegion;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public void setHomeDepot(String homeDepot) {
        this.homeDepot = homeDepot;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<BlockOnLoco> getBlockOnLocos() {
        return blockOnLocos;
    }

    public void setBlockOnLocos(List<BlockOnLoco> blockOnLocos) {
        this.blockOnLocos = blockOnLocos;
    }

    public void addBlockOnLoco(BlockOnLoco blockOnLoco) {
        blockOnLocos.add(blockOnLoco);
        blockOnLoco.setLocoList(this);
    }

    public void removeBlockOnLoco(BlockOnLoco blockOnLoco) {
        blockOnLocos.remove(blockOnLoco);
        blockOnLoco.setLocoList(null);
    }
}
