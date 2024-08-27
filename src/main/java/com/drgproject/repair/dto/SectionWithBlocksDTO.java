package com.drgproject.repair.dto;

import java.util.List;

public class SectionWithBlocksDTO {

    private String sectionNumber;
    private List<BlockOnLocoDTO> blocks;

    public SectionWithBlocksDTO(String sectionNumber, List<BlockOnLocoDTO> blocks) {
        this.sectionNumber = sectionNumber;
        this.blocks = blocks;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public List<BlockOnLocoDTO> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockOnLocoDTO> blocks) {
        this.blocks = blocks;
    }
}
