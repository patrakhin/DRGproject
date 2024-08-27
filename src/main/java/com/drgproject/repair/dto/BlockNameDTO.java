package com.drgproject.repair.dto;

public class BlockNameDTO {

    private Long id;
    private String blockName;
    private Long systemNameId;

    public BlockNameDTO() {}

    public BlockNameDTO(Long id, String blockName, Long systemNameId) {
        this.id = id;
        this.blockName = blockName;
        this.systemNameId = systemNameId;
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

    public Long getSystemNameId() {
        return systemNameId;
    }

    public void setSystemNameId(Long systemNameId) {
        this.systemNameId = systemNameId;
    }
}
