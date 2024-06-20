package com.drgproject.repair.dto;

public class HomeDepotDTO {
    private Long id;
    private String depot;
    private Long regionId;

    public HomeDepotDTO() {}

    public HomeDepotDTO(Long id, String depot, Long regionId) {
        this.id = id;
        this.depot = depot;
        this.regionId = regionId;
    }

    public HomeDepotDTO(String depot, Long regionId) {
        this.depot = depot;
        this.regionId = regionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
}
