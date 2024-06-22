package com.drgproject.repair.dto;

import java.util.List;

public class LocoListDTO {
    private Long id;
    private String contractNumber;
    private String typeLoco;
    private String typeSystem;
    private String locoNumber;
    private String comment;
    private List<Long> blockOnLocos;

    public LocoListDTO() {}

    public LocoListDTO(Long id, String contractNumber, String typeLoco, String typeSystem,
                       String locoNumber, String comment, List<Long> blockOnLocos) {
        this.id = id;
        this.contractNumber = contractNumber;
        this.typeLoco = typeLoco;
        this.typeSystem = typeSystem;
        this.locoNumber = locoNumber;
        this.comment = comment;
        this.blockOnLocos = blockOnLocos;
    }

    // Getters and Setters

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

    public List<Long> getBlockOnLocos() {
        return blockOnLocos;
    }

    public void setBlockOnLocos(List<Long> blockOnLocos) {
        this.blockOnLocos = blockOnLocos;
    }
}
