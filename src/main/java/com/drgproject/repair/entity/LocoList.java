package com.drgproject.repair.entity;

import jakarta.persistence.*;

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

    @Column(name = "comment")
    private String comment;

    public LocoList() {
    }

    public LocoList(String contractNumber, String typeLoco, String typeSystem, String locoNumber, String comment) {
        this.contractNumber = contractNumber;
        this.typeLoco = typeLoco;
        this.typeSystem = typeSystem;
        this.locoNumber = locoNumber;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
