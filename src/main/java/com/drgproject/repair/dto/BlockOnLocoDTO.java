package com.drgproject.repair.dto;

public class BlockOnLocoDTO {
    private Long id;
    private String blockName;
    private String blockNumber;
    private Long locoListId;

    public BlockOnLocoDTO() {}

    public BlockOnLocoDTO(Long id, String blockName, String blockNumber, Long locoListId) {
        this.id = id;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.locoListId = locoListId;
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
}
