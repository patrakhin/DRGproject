package com.drgproject.repair.dto;

public class BlockOnLocoDTO {
    private Long id;
    private String blockName;
    private String blockNumber;
    private String dateOfIssue;

    private Long locoListId;
    private String typeLoco;
    private String locoNumber;

    public BlockOnLocoDTO() {}

    public BlockOnLocoDTO(Long id, String blockName, String blockNumber, String dateOfIssue, Long locoListId, String typeLoco, String locoNumber) {
        this.id = id;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.dateOfIssue = dateOfIssue;
        this.locoListId = locoListId;
        this.typeLoco = typeLoco;
        this.locoNumber = locoNumber;
    }

    // Getters and Setters

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

    public Long getLocoListId() {
        return locoListId;
    }

    public void setLocoListId(Long locoListId) {
        this.locoListId = locoListId;
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

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }
}
