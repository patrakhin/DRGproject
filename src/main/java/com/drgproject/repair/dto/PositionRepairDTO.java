package com.drgproject.repair.dto;

public class PositionRepairDTO {
    private Long id;
    private String posRepair;

    public PositionRepairDTO() {}

    public PositionRepairDTO(Long id, String posRepair) {
        this.id = id;
        this.posRepair = posRepair;
    }

    public PositionRepairDTO(String posRepair) {
        this.posRepair = posRepair;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosRepair() {
        return posRepair;
    }

    public void setPosRepair(String posRepair) {
        this.posRepair = posRepair;
    }
}
